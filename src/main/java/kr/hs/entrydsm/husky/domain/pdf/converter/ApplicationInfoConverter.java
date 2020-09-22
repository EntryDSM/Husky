package kr.hs.entrydsm.husky.domain.pdf.converter;

import kr.hs.entrydsm.husky.domain.application.domain.CalculatedScore;
import kr.hs.entrydsm.husky.domain.application.domain.GeneralApplication;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GEDApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GeneralApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GraduatedApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.UnGraduatedApplicationRepository;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static kr.hs.entrydsm.husky.domain.user.domain.enums.AdditionalType.NATIONAL_MERIT;
import static kr.hs.entrydsm.husky.domain.user.domain.enums.AdditionalType.PRIVILEGED_ADMISSION;

@Component
@RequiredArgsConstructor
public class ApplicationInfoConverter {

    private final GEDApplicationRepository gedApplicationRepository;
    private final GeneralApplicationRepository generalApplicationRepository;
    private final GraduatedApplicationRepository graduatedApplicationRepository;
    private final UnGraduatedApplicationRepository unGraduatedApplicationRepository;

    public HashMap<String, String> applicationToInfo(User user, CalculatedScore calculatedScore) {
        HashMap<String, String> values = new HashMap<>();
        setReceiptCode(values, user);
        setPersonalInfo(values, user);
        setSchoolInfo(values, user);
        setPhoneNumber(values, user);
        setGraduationClassification(values, user);
        setUserType(values, user);
        setGradeScore(values, user, calculatedScore);
        setLocalDate(values);
        setIntroduction(values, user);
        setParentInfo(values, user);

        if (isRecommendationsRequired(user))
            setRecommendations(values, user);

        return values;
    }

    private void setReceiptCode(HashMap<String, String> values, User user) {
        values.put("receiptCode", user.getReceiptCode().toString());
    }

    private void setPersonalInfo(Map<String, String> values, User user) {
        values.put("userName", setBlankIfNull(user.getName()));
        values.put("isMale", toBallotBox(user.isMale()));
        values.put("isFemale", toBallotBox(user.isFemale()));
        values.put("address", setBlankIfNull(user.getAddress()));
        values.put("detailAddress", setBlankIfNull(user.getDetailAddress()));

        String birthDate = "";
        if (user.getBirthDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
            birthDate = user.getBirthDate().format(formatter);
        }
        values.put("birthDate", birthDate);
    }

    private void setSchoolInfo(Map<String, String> values, User user) {
        GeneralApplication generalApplication = getGeneralApplication(user);
        if (!user.isGradeTypeEmpty() && !user.isGED() && generalApplication != null) {
            values.put("schoolCode", setBlankIfNull(generalApplication.getSchoolCode()));
            values.put("schoolClass", setBlankIfNull(generalApplication.getSchoolClass()));
            values.put("schoolTel", setBlankIfNull(toFormattedPhoneNumber(generalApplication.getSchoolTel())));
            values.put("schoolName", setBlankIfNull(generalApplication.getSchoolName()));
        }
    }

    private void setPhoneNumber(Map<String, String> values, User user) {
        values.put("applicantTel", toFormattedPhoneNumber(user.getApplicantTel()));
        values.put("parentTel", toFormattedPhoneNumber(user.getParentTel()));
        String homeTel = (user.isHomeTelEmpty()) ? "없음" : toFormattedPhoneNumber(user.getHomeTel());
        values.put("homeTel", toFormattedPhoneNumber(homeTel));
    }

    private void setGraduationClassification(HashMap<String, String> values, User user) {
        values.putAll(emptyGraduationClassification());

        switch (user.getGradeType()) {
            case GED:
                gedApplicationRepository.findById(user.getReceiptCode())
                        .filter(ged -> ged.getGedPassDate() != null)
                        .ifPresent(ged -> {
                            values.put("gedPassedYear", String.valueOf(ged.getGedPassDate().getYear()));
                            values.put("gedPassedMonth", String.valueOf(ged.getGedPassDate().getMonth()));
                        });
                break;

            case GRADUATED:
                graduatedApplicationRepository.findById(user.getReceiptCode())
                        .filter(graduated -> graduated.getGraduatedDate() != null)
                        .ifPresent(graduated -> {
                            values.put("graduatedYear", String.valueOf(graduated.getGraduatedDate().getYear()));
                            values.put("graduatedMonth", String.valueOf(graduated.getGraduatedDate().getMonth()));
                        });
                break;

            case UNGRADUATED:
                unGraduatedApplicationRepository.findById(user.getReceiptCode())
                        .filter(unGraduated -> unGraduated.getGraduatedDate() != null)
                        .ifPresent(unGraduated -> {
                            values.put("unGraduatedMonth", String.valueOf(unGraduated.getGraduatedDate().getMonth()));
                        });
        }
    }

    private Map<String, String> emptyGraduationClassification() {
        return Map.of(
                "gedPassedYear", "",
                "gedPassedMonth", "",
                "graduatedYear", "",
                "graduatedMonth", "",
                "unGraduatedMonth", ""
        );
    }

    private void setUserType(Map<String, String> values, User user) {
        values.put("isUnGraduated", toBallotBox(user.isUngraduated()));
        values.put("isGraduated", toBallotBox(user.isGraduated()));
        values.put("isGed", toBallotBox(user.isGED()));
        values.put("isDj", toBallotBox(user.getIsDaejeon()));
        values.put("isNotDaejeon", toBallotBox(!user.getIsDaejeon()));
        values.put("isNationalMerit", toBallotBox(user.isAdditionalTypeEquals(NATIONAL_MERIT)));
        values.put("isPrivilegedAdmission", toBallotBox(user.isAdditionalTypeEquals(PRIVILEGED_ADMISSION)));
        values.put("isCommon", toBallotBox(user.isCommonApplyType()));
        values.put("isMeister", toBallotBox(user.isMeisterApplyType()));
        values.put("isSocialMerit", toBallotBox(user.isSocialMeritApplytype()));
    }

    private void setGradeScore(Map<String, String> values, User user, CalculatedScore calculatedScore) {
        values.put("conversionScore1st", (user.isGED()) ? "" : calculatedScore.getFirstGradeScore().toString());
        values.put("conversionScore2nd", (user.isGED()) ? "" : calculatedScore.getSecondGradeScore().toString());
        values.put("conversionScore3rd", (user.isGED()) ? "" : calculatedScore.getThirdGradeScore().toString());
        values.put("conversionScore", calculatedScore.getConversionScore().toString());
        values.put("attendanceScore", calculatedScore.getAttendanceScore().toString());
        values.put("volunteerScore", calculatedScore.getVolunteerScore().toString());
        values.put("finalScore", calculatedScore.getFinalScore().toString());
    }

    private void setLocalDate(HashMap<String, String> values) {
        LocalDateTime now = LocalDateTime.now();
        values.put("month", String.valueOf(now.getMonthValue()));
        values.put("day", String.valueOf(now.getDayOfMonth()));
    }

    private void setIntroduction(HashMap<String, String> values, User user) {
        values.put("selfIntroduction", setBlankIfNull(replaceNewlineToken(user.getSelfIntroduction())));
        values.put("studyPlan", setBlankIfNull(replaceNewlineToken(user.getStudyPlan())));
    }

    private String replaceNewlineToken(String string) {
        return (string != null) ? string.replace("\n", "¶") : null;
    }

    private void setParentInfo(HashMap<String, String> values, User user) {
        values.put("parentName", user.getParentName());
    }

    private void setRecommendations(HashMap<String, String> values, User user) {
        values.put("isDaejeonAndMeister", markOIfTrue(user.getIsDaejeon() && user.isMeisterApplyType()));
        values.put("isDaejeonAndSocialMerit", markOIfTrue(user.getIsDaejeon() && user.isSocialMeritApplytype()));
        values.put("isNotDaejeonAndMeister", markOIfTrue(!user.getIsDaejeon() && user.isMeisterApplyType()));
        values.put("isNotDaejeonAndSocialMerit", markOIfTrue(!user.getIsDaejeon() && user.isMeisterApplyType()));
        setSpaceJoinedSchoolName(values, user);
    }

    private String markOIfTrue(boolean isTrue) {
        return (isTrue) ? "◯" : "";
    }

    public void setSpaceJoinedSchoolName(Map<String, String> values, User user) {
        if (!isRecommendationsRequired(user)) return;

        StringBuilder spaceJoinedSchoolName = new StringBuilder();
        if (isSchoolNameExists(user)) {
            String schoolName = getGeneralApplication(user).getSchoolName();
            if (isSchoolNameLongerThan10Char(schoolName)) {
                spaceJoinedSchoolName.append(schoolName).append("장");
            } else {
                spaceJoinedSchoolName.append(getTrimmedSchoolName(schoolName)).append("장");
            }
        }

        values.put("spaceJoinedSchoolName", spaceJoinedSchoolName.toString());
    }

    private boolean isRecommendationsRequired(User user) {
        return !user.isGradeTypeEmpty() && !user.isGED() && !user.isCommonApplyType();
    }

    private boolean isSchoolNameExists(User user) {
        return !user.isGradeTypeEmpty() && !user.isGED()
                && generalApplicationRepository.existsByUser(user)
                && getGeneralApplication(user).getSchoolName() != null;
    }

    private boolean isSchoolNameLongerThan10Char(String schoolName) {
        return schoolName.length() > 10;
    }

    private String toBallotBox(boolean is) {
        return (is) ? "☑" : "☐";
    }

    private String toFormattedPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return "";
        } else {
            return phoneNumber.replace("-", " - ");
        }
    }

    private String getTrimmedSchoolName(String schoolName) {
        return schoolName.replaceAll("(.)", "$0 ").trim();
    }

    public String setBlankIfNull(String input) {
        return (input == null) ? "" : input;
    }

    private GeneralApplication getGeneralApplication(User user) {
        return generalApplicationRepository.findByUser(user);
    }

}
