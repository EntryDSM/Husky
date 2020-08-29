package kr.hs.entrydsm.husky.domain.pdf.converter;

import kr.hs.entrydsm.husky.domain.application.domain.CalculatedScore;
import kr.hs.entrydsm.husky.domain.school.domain.School;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.enums.AdditionalType;
import kr.hs.entrydsm.husky.domain.user.domain.enums.ApplyType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class ApplicationInfoConverter {

    public static HashMap<String, String> toMap(User user, CalculatedScore calculatedScore) {
        HashMap<String, String> values = new HashMap<>();
        values.put("receiptCode", user.getReceiptCode().toString());
        values.put("schoolCode", (user.isGED()) ? "" : user.getGeneralApplication().getSchool().getSchoolCode());
        values.put("schoolClass", (user.isGED()) ? "" : toSchoolClass(user.getGeneralApplication().getStudentNumber()));
        values.put("userName", setBlankIfNull(user.getName()));
        values.put("birthDate", (user.getBirthDate() == null) ? "" : user.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")));
        values.put("isMale", toBallotBox(user.isMale()));
        values.put("isFemale", toBallotBox(user.isFemale()));
        values.put("address", setBlankIfNull(user.getAddress()));
        values.put("parentTel", (user.getParentTel() == null) ? "" : toFormattedPhoneNumber(user.getParentTel()));
        values.put("schoolTel", (user.isGED()) ? "" : toFormattedPhoneNumber(user.getGeneralApplication().getSchoolTel()));
        values.put("homeTel", toFormattedPhoneNumber(user.getHomeTel()));
        values.put("applicantTel", toFormattedPhoneNumber(user.getApplicantTel()));
        values.put("isUnGraduated", toBallotBox(user.isUngraduated()));
        values.put("isGraduated", toBallotBox(user.isGraduated()));
        values.put("isGed", toBallotBox(user.isGED()));
        values.put("isDj", toBallotBox(user.isDaejeon()));
        values.put("isNotDaejeon", toBallotBox(!user.isDaejeon()));
        values.put("isNationalMerit", toBallotBox(user.getAdditionalType().equals(AdditionalType.NATIONAL_MERIT)));
        values.put("isPrivilegedAdmission", toBallotBox(user.getAdditionalType().equals(AdditionalType.PRIVILEGED_ADMISSION)));
        values.put("isCommon", toBallotBox(isCommon(user)));
        values.put("isMeister", toBallotBox(isMeister(user)));
        values.put("isSocialMerit", toBallotBox(isSocialMerit(user)));
        values.put("conversionScore1st", (user.isGED()) ? "" : calculatedScore.getFirstGradeScore().toString());
        values.put("conversionScore2nd", (user.isGED()) ? "" : calculatedScore.getSecondGradeScore().toString());
        values.put("conversionScore3rd", (user.isGED()) ? "" : calculatedScore.getThirdGradeScore().toString());
        values.put("conversionScore", calculatedScore.getConversionScore().toString());
        values.put("attendanceScore", calculatedScore.getAttendanceScore().toString());
        values.put("volunteerScore", calculatedScore.getVolunteerScore().toString());
        values.put("finalScore", calculatedScore.getFinalScore().toString());
        values.put("month", String.valueOf(LocalDateTime.now().getMonthValue()));
        values.put("day", String.valueOf(LocalDateTime.now().getDayOfMonth()));
        values.put("schoolName", (user.isGED()) ? "" : user.getGeneralApplication().getSchool().getSchoolName());
        values.put("spaceJoinedSchoolName", (user.isGED()) ? "" : getTrimmedSchoolName(user.getGeneralApplication().getSchool()));
        values.put("selfIntroduction", (user.getSelfIntroduction() == null) ? "" : user.getSelfIntroduction());
        values.put("studyPlan", (user.getStudyPlan() == null) ? "" : user.getStudyPlan());
        values.put("parentName", user.getParentName());
        values.put("isDaejeonAndMeister", toBallotBox(user.isDaejeon() && user.getApplyType().equals(ApplyType.MEISTER)));
        values.put("isDaejeonAndSocialMerit", toBallotBox(user.isDaejeon() && isSocialMerit(user)));
        values.put("isNotDaejeonAndMeister", toBallotBox(!user.isDaejeon() && isMeister(user)));
        values.put("isNotDaejeonAndSocialMerit", toBallotBox(!user.isDaejeon() && isSocialMerit(user)));

        return values;
    }

    private static String toSchoolClass(String studentNumber) {
        return studentNumber.substring(1, 2).replace("0", "");
    }

    private static String toBallotBox(boolean is) {
        return (is) ? "\u2611" : "\u2610";
    }

    private static String toFormattedPhoneNumber(String phoneNumber) {
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

    private static String getTrimmedSchoolName(School school) {
        return school.getSchoolName().replaceAll("(.)", "$0 ").trim();
    }

    public static String setBlankIfNull(String input) {
        return (input == null) ? "" : input;
    }

}
