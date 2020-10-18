package kr.hs.entrydsm.husky.domain.pdf.converter;

import kr.hs.entrydsm.husky.domain.application.domain.CalculatedScore;
import kr.hs.entrydsm.husky.domain.application.domain.GeneralApplication;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GEDApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GeneralApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GraduatedApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.UnGraduatedApplicationRepository;
import kr.hs.entrydsm.husky.domain.image.service.ImageService;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.global.util.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
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

    private final ImageService imageService;

    public Map<String, Object> applicationToInfo(User user, CalculatedScore calculatedScore) throws IOException {
        Map<String, Object> values = new HashMap<>();
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

        if (Validator.isExists(user.getUserPhoto()))
            setBase64Image(values, user);

        return values;
    }

    private void setReceiptCode(Map<String, Object> values, User user) {
        values.put("receiptCode", user.getReceiptCode().toString());
    }

    private void setPersonalInfo(Map<String, Object> values, User user) {
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

        String gender;
        switch (user.getSex()) {
            case FEMALE:
                gender = "여";
                break;

            case MALE:
                gender = "남";
                break;

            default:
                gender = null;
        }
        values.put("gender", setBlankIfNull(gender));
    }

    private void setSchoolInfo(Map<String, Object> values, User user) {
        GeneralApplication generalApplication = getGeneralApplication(user);
        if (!user.isGradeTypeEmpty() && !user.isGED() && generalApplication != null) {
            values.put("schoolCode", setBlankIfNull(generalApplication.getSchoolCode()));
            values.put("schoolClass", setBlankIfNull(generalApplication.getSchoolClass()));
            values.put("schoolTel", toFormattedPhoneNumber(generalApplication.getSchoolTel()));
            values.put("schoolName", setBlankIfNull(generalApplication.getSchoolName()));
        } else {
            values.putAll(emptySchoolInfo());
        }
    }

    private Map<String, Object> emptySchoolInfo() {
        return Map.of(
                "schoolCode", "",
                "schoolClass", "",
                "schoolTel", "",
                "schoolName", ""
        );
    }

    private void setPhoneNumber(Map<String, Object> values, User user) {
        values.put("applicantTel", toFormattedPhoneNumber(user.getApplicantTel()));
        values.put("parentTel", toFormattedPhoneNumber(user.getParentTel()));
        String homeTel = (user.isHomeTelEmpty()) ? "없음" : toFormattedPhoneNumber(user.getHomeTel());
        values.put("homeTel", toFormattedPhoneNumber(homeTel));
    }

    private void setGraduationClassification(Map<String, Object> values, User user) {
        values.putAll(emptyGraduationClassification());

        switch (user.getGradeType()) {
            case GED:
                gedApplicationRepository.findById(user.getReceiptCode())
                        .filter(ged -> ged.getGedPassDate() != null)
                        .ifPresent(ged -> {
                            values.put("gedPassedYear", String.valueOf(ged.getGedPassDate().getYear()));
                            values.put("gedPassedMonth", String.valueOf(ged.getGedPassDate().getMonthValue()));
                        });
                break;

            case GRADUATED:
                graduatedApplicationRepository.findById(user.getReceiptCode())
                        .filter(graduated -> graduated.getGraduatedDate() != null)
                        .ifPresent(graduated -> {
                            values.put("graduatedYear", String.valueOf(graduated.getGraduatedDate().getYear()));
                            values.put("graduatedMonth", String.valueOf(graduated.getGraduatedDate().getMonthValue()));
                        });
                break;

            case UNGRADUATED:
                unGraduatedApplicationRepository.findById(user.getReceiptCode())
                        .filter(unGraduated -> unGraduated.getGraduatedDate() != null)
                        .ifPresent(unGraduated -> {
                            values.put("unGraduatedMonth", String.valueOf(unGraduated.getGraduatedDate().getMonthValue()));
                        });
        }
    }

    private Map<String, Object> emptyGraduationClassification() {
        return Map.of(
                "gedPassedYear", "20__",
                "gedPassedMonth", "__",
                "graduatedYear", "20__",
                "graduatedMonth", "__",
                "unGraduatedMonth", "__"
        );
    }

    private void setUserType(Map<String, Object> values, User user) {
        values.put("isUnGraduated", toBallotBox(user.isUngraduated()));
        values.put("isGraduated", toBallotBox(user.isGraduated()));
        values.put("isGed", toBallotBox(user.isGED()));
        values.put("isDaejeon", toBallotBox(user.getIsDaejeon()));
        values.put("isNotDaejeon", toBallotBox(!user.getIsDaejeon()));
        values.put("isNationalMerit", toBallotBox(user.isAdditionalTypeEquals(NATIONAL_MERIT)));
        values.put("isPrivilegedAdmission", toBallotBox(user.isAdditionalTypeEquals(PRIVILEGED_ADMISSION)));
        values.put("isCommon", toBallotBox(user.isCommonApplyType()));
        values.put("isMeister", toBallotBox(user.isMeisterApplyType()));
        values.put("isSocialMerit", toBallotBox(user.isSocialMeritApplyType()));
    }

    private void setGradeScore(Map<String, Object> values, User user, CalculatedScore calculatedScore) {
        values.put("conversionScore1st", (user.isGED()) ? "" : calculatedScore.getFirstGradeScore().toString());
        values.put("conversionScore2nd", (user.isGED()) ? "" : calculatedScore.getSecondGradeScore().toString());
        values.put("conversionScore3rd", (user.isGED()) ? "" : calculatedScore.getThirdGradeScore().toString());
        values.put("conversionScore", calculatedScore.getConversionScore().toString());
        values.put("attendanceScore", calculatedScore.getAttendanceScore().toString());
        values.put("volunteerScore", calculatedScore.getVolunteerScore().toString());
        values.put("finalScore", calculatedScore.getFinalScore().toString());
    }

    private void setLocalDate(Map<String, Object> values) {
        LocalDateTime now = LocalDateTime.now();
        values.put("year", String.valueOf(now.getYear()));
        values.put("month", String.valueOf(now.getMonthValue()));
        values.put("day", String.valueOf(now.getDayOfMonth()));
    }

    private void setIntroduction(Map<String, Object> values, User user) {
        values.put("selfIntroduction", setBlankIfNull(user.getSelfIntroduction()));
        values.put("studyPlan", setBlankIfNull(user.getStudyPlan()));
        values.put("newLineChar", "\n");
    }

    private void setParentInfo(Map<String, Object> values, User user) {
        values.put("parentName", user.getParentName());
    }

    private void setRecommendations(Map<String, Object> values, User user) {
        values.put("isDaejeonAndMeister", markOIfTrue(user.getIsDaejeon() && user.isMeisterApplyType()));
        values.put("isDaejeonAndSocialMerit", markOIfTrue(user.getIsDaejeon() && user.isSocialMeritApplyType()));
        values.put("isNotDaejeonAndMeister", markOIfTrue(!user.getIsDaejeon() && user.isMeisterApplyType()));
        values.put("isNotDaejeonAndSocialMerit", markOIfTrue(!user.getIsDaejeon() && user.isSocialMeritApplyType()));
    }

    private String markOIfTrue(boolean isTrue) {
        return (isTrue) ? "◯" : "";
    }

    private void setBase64Image(Map<String, Object> values, User user) throws IOException {
        byte[] image = imageService.getObject(user.getUserPhoto());
        String base64EncodedImage = new String(Base64.getEncoder().encode(image), "UTF-8");
        values.put("base64Image", base64EncodedImage);
    }

    private boolean isRecommendationsRequired(User user) {
        return !user.isGradeTypeEmpty() && !user.isGED() && !user.isCommonApplyType();
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

    public String setBlankIfNull(String input) {
        return (input == null) ? "" : input;
    }

    private GeneralApplication getGeneralApplication(User user) {
        return generalApplicationRepository.findByUser(user);
    }

}
