package kr.hs.entrydsm.husky.domain.pdf.converter;

import kr.hs.entrydsm.husky.domain.application.domain.CalculatedScore;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.enums.AdditionalType;
import kr.hs.entrydsm.husky.domain.user.domain.enums.ApplyType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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
        setSpaceJoinedSchoolName(values, user);
        setParentInfo(values, user);
        setRecommendations(values, user);

        return values;
    }

    private static void setReceiptCode(HashMap<String, String> values, User user) {
        values.put("receiptCode", user.getReceiptCode().toString());
    }

    private static void setLocalDate(HashMap<String, String> values) {
        values.put("month", String.valueOf(LocalDateTime.now().getMonthValue()));
        values.put("day", String.valueOf(LocalDateTime.now().getDayOfMonth()));
    }

    private static void setRecommendations(HashMap<String, String> values, User user) {
        values.put("isDaejeonAndMeister", toBallotBox(user.getIsDaejeon() && user.getApplyType().equals(ApplyType.MEISTER)));
        values.put("isDaejeonAndSocialMerit", toBallotBox(user.getIsDaejeon() && isSocialMerit(user)));
        values.put("isNotDaejeonAndMeister", toBallotBox(!user.getIsDaejeon() && isMeister(user)));
        values.put("isNotDaejeonAndSocialMerit", toBallotBox(!user.getIsDaejeon() && isSocialMerit(user)));
    }

    private static void setParentInfo(HashMap<String, String> values, User user) {
        values.put("parentName", user.getParentName());
    }

    private static void setIntroducement(HashMap<String, String> values, User user) {
        values.put("selfIntroduction", (user.getSelfIntroduction() == null) ? "" : user.getSelfIntroduction());
        values.put("studyPlan", (user.getStudyPlan() == null) ? "" : user.getStudyPlan());
    }

    private static void setPhoneNumber(Map<String, String> values, User user) {
        values.put("homeTel", toFormattedPhoneNumber(user.getHomeTel()));
        values.put("applicantTel", toFormattedPhoneNumber(user.getApplicantTel()));
        values.put("parentTel", toFormattedPhoneNumber(user.getParentTel()));
    }

    private static void setUserType(Map<String, String> values, User user) {
        values.put("isUnGraduated", toBallotBox(user.isUngraduated()));
        values.put("isGraduated", toBallotBox(user.isGraduated()));
        values.put("isGed", toBallotBox(user.isGED()));
        values.put("isDj", toBallotBox(user.getIsDaejeon()));
        values.put("isNotDaejeon", toBallotBox(!user.getIsDaejeon()));
        values.put("isNationalMerit", toBallotBox(user.getAdditionalType().equals(AdditionalType.NATIONAL_MERIT)));
        values.put("isPrivilegedAdmission", toBallotBox(user.getAdditionalType().equals(AdditionalType.PRIVILEGED_ADMISSION)));
        values.put("isCommon", toBallotBox(isCommon(user)));
        values.put("isMeister", toBallotBox(isMeister(user)));
        values.put("isSocialMerit", toBallotBox(isSocialMerit(user)));
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
        if (user.isGED()) {
            values.put("schoolCode", "");
            values.put("schoolClass", "");
            values.put("schoolTel", "");
            values.put("schoolName", "");
        } else {
            values.put("schoolCode", user.getGeneralApplication().getSchoolCode());
            values.put("schoolClass", user.getGeneralApplication().getSchoolClass());
            values.put("schoolTel", user.getGeneralApplication().getSchoolTel());
            values.put("schoolName", user.getGeneralApplication().getSchoolName());
        }
    }

    public static void setSpaceJoinedSchoolName(Map<String, String> values, User user) {
        if (!user.isGED() && user.getGeneralApplication().getSchoolName() != null)
            if (user.getGeneralApplication().getSchoolName().length() < 10)
                values.put("spaceJoinedSchoolName", getTrimmedSchoolName(user.getGeneralApplication().getSchoolName()).concat("장"));
            else
                values.put("spaceJoinedSchoolName", user.getGeneralApplication().getSchoolName().concat("장"));
    }

    private static String toBallotBox(boolean is) {
        return (is) ? "☑" : "☐";
    }

    private static String toFormattedPhoneNumber(String phoneNumber) {
        if (phoneNumber == null)
            return "";
        return phoneNumber.replace("-", " - ");
    }

    private static boolean isSocialMerit(User user) {
        return !user.getApplyType().equals(ApplyType.MEISTER) && !user.getApplyType().equals(ApplyType.COMMON);
    }

    private static boolean isMeister(User user) {
        return user.getApplyType().equals(ApplyType.MEISTER);
    }

    private static boolean isCommon(User user) {
        return user.getApplyType().equals(ApplyType.COMMON);
    }

    private static String getTrimmedSchoolName(String schoolName) {
        return schoolName.replaceAll("(.)", "$0 ").trim();
    }

    public static String setBlankIfNull(String input) {
        return (input == null) ? "" : input;
    }

}
