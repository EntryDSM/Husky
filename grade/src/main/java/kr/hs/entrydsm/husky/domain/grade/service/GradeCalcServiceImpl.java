package kr.hs.entrydsm.husky.domain.grade.service;

import kr.hs.entrydsm.husky.domain.grade.exception.UserNotFoundException;
import kr.hs.entrydsm.husky.domain.grade.util.GradeUtil;
import kr.hs.entrydsm.husky.domain.grade.value.GradeMatrix;
import kr.hs.entrydsm.husky.entities.applications.CalculatedScore;
import kr.hs.entrydsm.husky.entities.applications.GeneralApplication;
import kr.hs.entrydsm.husky.entities.applications.repositories.CalculatedScoreRepository;
import kr.hs.entrydsm.husky.entities.applications.value.GradeScore;
import kr.hs.entrydsm.husky.entities.users.User;
import kr.hs.entrydsm.husky.entities.users.enums.ApplyType;
import kr.hs.entrydsm.husky.entities.users.enums.GradeType;
import kr.hs.entrydsm.husky.entities.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.math.RoundingMode.CEILING;
import static java.math.RoundingMode.HALF_UP;
import static kr.hs.entrydsm.husky.domain.grade.constant.BigDecimalConstants.SIX;
import static kr.hs.entrydsm.husky.domain.grade.constant.Semester.*;

@Service
@RequiredArgsConstructor
public class GradeCalcServiceImpl implements GradeCalcService {

    public static final int DEFAULT_ATTENDANCE_SCORE = 15;

    private final UserRepository userRepository;
    private final CalculatedScoreRepository calculatedScoreRepository;

    @Override
    public CalculatedScore calcStudentGrade(int receiptCode) {
        User user = userRepository.findByReceiptCode(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        int attendanceScore = calcAttendanceScore(user);
        BigDecimal volunteerScore = calcVolunteerScore(user);
        GradeScore gradeScore = calcGradeScore(user);
        BigDecimal finalScore = calcFinalScore(attendanceScore, volunteerScore, gradeScore);

        return calculatedScoreRepository.save(CalculatedScore.builder()
                .user(user)
                .attendanceScore(attendanceScore)
                .volunteerScore(volunteerScore)
                .gradeScore(gradeScore)
                .finalScore(finalScore)
                .build());
    }

    private Integer calcAttendanceScore(User user) {
        if (user.getGradeType() == GradeType.GED) {
            return DEFAULT_ATTENDANCE_SCORE;
        }

        GeneralApplication app = (GeneralApplication) user.getApplication();
        int conversionAbsence = app.getFullCutCount() +
                (app.getLateCount() + app.getEarlyLeaveCount() + app.getPeriodCutCount()) / 3;

        return DEFAULT_ATTENDANCE_SCORE - conversionAbsence;
    }

    private BigDecimal calcVolunteerScore(User user) {
        if (user.getGradeType() == GradeType.GED) {
            return BigDecimal.valueOf(user.getGedApplication().getGedAverageScore())
                    .subtract(BigDecimal.valueOf(40))
                    .divide(BigDecimal.valueOf(5), 3, CEILING)
                    .add(BigDecimal.valueOf(3));
        }

        GeneralApplication app = (GeneralApplication) user.getApplication();
        if (app.getVolunteerTime() >= 45) {
            return BigDecimal.valueOf(15);
        }

        if (app.getVolunteerTime() <= 9) {
            return BigDecimal.valueOf(3);
        }

        return BigDecimal.valueOf(app.getVolunteerTime())
                .subtract(BigDecimal.valueOf(9))
                .divide(BigDecimal.valueOf(3), 3, CEILING)
                .add(BigDecimal.valueOf(3))
                .setScale(3, HALF_UP);
    }

    private GradeScore calcGradeScore(User user) {
        GradeScore gradeScore;

        if (user.getGradeType() == GradeType.GED) {
            BigDecimal conversionScore = BigDecimal.valueOf(user.getGedApplication().getGedAverageScore())
                    .subtract(BigDecimal.valueOf(50))
                    .divide(BigDecimal.valueOf(50), 3, CEILING)
                    .multiply(BigDecimal.valueOf(150))
                    .setScale(3, HALF_UP);
            gradeScore = new GradeScore(conversionScore);

        } else {
            gradeScore = this.calcGeneralApplication(user);
        }

        if (user.getApplyType() != ApplyType.COMMON) {
            gradeScore.setConversionScore(gradeScore.getConversionScore().multiply(BigDecimal.valueOf(0.6))
                    .setScale(3, HALF_UP));
        }

        return gradeScore;
    }

    private BigDecimal calcFinalScore(int attendanceScore, BigDecimal volunteerScore, GradeScore gradeScore) {
        return BigDecimal.valueOf(attendanceScore)
                .add(volunteerScore)
                .add(gradeScore.getConversionScore());
    }

    private GradeScore calcGeneralApplication(User user) {
        GeneralApplication application = (GeneralApplication) user.getApplication();
        GradeMatrix matrix = new GradeMatrix(application);
        GradeUtil matrixUtil = new GradeUtil(user, matrix);

        BigDecimal firstGradeScore = null;
        BigDecimal secondGradeScore = null;
        BigDecimal thirdGradeScore;

        boolean isFirstGradeEmpty = matrixUtil.isFirstGradeEmpty();
        boolean isSecondGradeEmpty = matrixUtil.isSecondGradeEmpty();

        matrixUtil.fillEmptySemester();

        if (isFirstGradeEmpty && isSecondGradeEmpty) {
            firstGradeScore = matrixUtil.getAverageScoreIf3rdGradeLeft();
            secondGradeScore = firstGradeScore;
        }

        else if (isFirstGradeEmpty && !isSecondGradeEmpty)
            firstGradeScore = matrixUtil.getAverageScoreIf1stGradeEmpty();

        else if (!isFirstGradeEmpty && isSecondGradeEmpty)
            secondGradeScore = matrixUtil.getAverageScoreIf2ndGradeEmpty();

        if (firstGradeScore == null)
            firstGradeScore = matrixUtil.getScore(SEMESTER_1_1, SEMESTER_1_2);

        if (secondGradeScore == null)
            secondGradeScore = matrixUtil.getScore(SEMESTER_2_1, SEMESTER_2_2);

        thirdGradeScore = matrixUtil.getScore(SEMESTER_3_1, SEMESTER_3_2);

        firstGradeScore = firstGradeScore.multiply(BigDecimal.valueOf(4.5)).setScale(3, HALF_UP);
        secondGradeScore = secondGradeScore.multiply(BigDecimal.valueOf(4.5)).setScale(3, HALF_UP);
        thirdGradeScore = thirdGradeScore.multiply(SIX).setScale(3, HALF_UP);

        BigDecimal conversionScore = firstGradeScore
                .add(secondGradeScore)
                .add(thirdGradeScore);

        return GradeScore.builder()
                .firstGradeScore(firstGradeScore)
                .secondGradeScore(secondGradeScore)
                .thirdGradeScore(thirdGradeScore)
                .conversionScore(conversionScore)
                .build();
    }

}
