package kr.hs.entrydsm.husky.domain.pdf.converter;

import kr.hs.entrydsm.husky.domain.application.domain.CalculatedScore;
import kr.hs.entrydsm.husky.domain.user.domain.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static kr.hs.entrydsm.husky.domain.user.domain.enums.AdditionalType.NATIONAL_MERIT;
import static kr.hs.entrydsm.husky.domain.user.domain.enums.AdditionalType.PRIVILEGED_ADMISSION;

public class ApplicationInfoConverter {

    public static HashMap<String, String> applicationToInfo(User user, CalculatedScore calculatedScore) {
        HashMap<String, String> values = new HashMap<>();
        setReceiptCode(values, user);
        setPersonalInfo(values, user);
        setSchoolInfo(values, user);
        setPhoneNumber(values, user);
        setUserType(values, user);
        setGradeScore(values, user, calculatedScore);
        setLocalDate(values);
        setIntroducement(values, user);
        setParentInfo(values, user);

        if (isRecommendationsRequired(user))
            setRecommendations(values, user);

        return values;
    }

    private static void setReceiptCode(HashMap<String, String> values, User user) {
        values.put("receiptCode", user.getReceiptCode().toString());
    }

    private static void setLocalDate(HashMap<String, String> values) {
        LocalDateTime now = LocalDateTime.now();
        values.put("month", String.valueOf(now.getMonthValue()));
        values.put("day", String.valueOf(now.getDayOfMonth()));
    }

    private static void setRecommendations(HashMap<String, String> values, User user) {
        values.put("isDaejeonAndMeister", markOIfTrue(user.getIsDaejeon() && user.isMeisterApplyType()));
        values.put("isDaejeonAndSocialMerit", markOIfTrue(user.getIsDaejeon() && user.isSocialMeritApplytype()));
        values.put("isNotDaejeonAndMeister", markOIfTrue(!user.getIsDaejeon() && user.isMeisterApplyType()));
        values.put("isNotDaejeonAndSocialMerit", markOIfTrue(!user.getIsDaejeon() && user.isMeisterApplyType()));
        setSpaceJoinedSchoolName(values, user);
    }

    private static String markOIfTrue(boolean isTrue) {
        return (isTrue) ? "◯" : "";
    }

    private static void setParentInfo(HashMap<String, String> values, User user) {
        values.put("parentName", user.getParentName());
    }

    private static void setIntroducement(HashMap<String, String> values, User user) {
        values.put("selfIntroduction", (user.getSelfIntroduction() == null) ? "" : user.getSelfIntroduction());
        values.put("studyPlan", (user.getStudyPlan() == null) ? "" : user.getStudyPlan());
    }

    private static void setPhoneNumber(Map<String, String> values, User user) {
        values.put("applicantTel", toFormattedPhoneNumber(user.getApplicantTel()));
        values.put("parentTel", toFormattedPhoneNumber(user.getParentTel()));
        String homeTel = (user.isHomeTelEmpty()) ? "없음" : toFormattedPhoneNumber(user.getHomeTel());
        values.put("homeTel", homeTel);
    }

    private static void setUserType(Map<String, String> values, User user) {
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

    private static void setGradeScore(Map<String, String> values, User user, CalculatedScore calculatedScore) {
        values.put("conversionScore1st", (user.isGED()) ? "" : calculatedScore.getFirstGradeScore().toString());
        values.put("conversionScore2nd", (user.isGED()) ? "" : calculatedScore.getSecondGradeScore().toString());
        values.put("conversionScore3rd", (user.isGED()) ? "" : calculatedScore.getThirdGradeScore().toString());
        values.put("conversionScore", calculatedScore.getConversionScore().toString());
        values.put("attendanceScore", calculatedScore.getAttendanceScore().toString());
        values.put("volunteerScore", calculatedScore.getVolunteerScore().toString());
        values.put("finalScore", calculatedScore.getFinalScore().toString());
    }

    private static void setPersonalInfo(Map<String, String> values, User user) {
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

    private static void setSchoolInfo(Map<String, String> values, User user) {
        if (!user.isGradeTypeEmpty() && !user.isGED() && user.getGeneralApplication() != null) {
            values.put("schoolCode", setBlankIfNull(user.getGeneralApplication().getSchoolCode()));
            values.put("schoolClass", setBlankIfNull(user.getGeneralApplication().getSchoolClass()));
            values.put("schoolTel", setBlankIfNull(user.getGeneralApplication().getSchoolTel()));
            values.put("schoolName", setBlankIfNull(user.getGeneralApplication().getSchoolName()));
        }
    }

    public static void setSpaceJoinedSchoolName(Map<String, String> values, User user) {
        if (!isRecommendationsRequired(user)) return;

        StringBuilder spaceJoinedSchoolName = new StringBuilder();
        if (isSchoolNameNotEmpty(user)) {
            String schoolName = user.getGeneralApplication().getSchoolName();
            if (isSchoolNameLongerThan10Char(schoolName)) {
                spaceJoinedSchoolName.append(schoolName).append("장");
            } else {
                spaceJoinedSchoolName.append(getTrimmedSchoolName(schoolName)).append("장");
            }
        }

        values.put("spaceJoinedSchoolName", spaceJoinedSchoolName.toString());
    }

    private static boolean isRecommendationsRequired(User user) {
        return !user.isGradeTypeEmpty() && !user.isGED() && !user.isCommonApplyType();
    }

    private static boolean isSchoolNameNotEmpty(User user) {
        return !user.isGradeTypeEmpty() && !user.isGED() &&
                !user.isGeneralApplicationEmpty() && user.getGeneralApplication().getSchoolName() != null;
    }

    private static boolean isSchoolNameLongerThan10Char(String schoolName) {
        return schoolName.length() > 10;
    }

    private static String toBallotBox(boolean is) {
        return (is) ? "☑" : "☐";
    }

    private static String toFormattedPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return "";
        } else {
            return phoneNumber.replace("-", " - ");
        }
    }

    private static String getTrimmedSchoolName(String schoolName) {
        return schoolName.replaceAll("(.)", "$0 ").trim();
    }

    public static String setBlankIfNull(String input) {
        return (input == null) ? "" : input;
    }

}
