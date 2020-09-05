package kr.hs.entrydsm.husky.domain.grade.service;

import kr.hs.entrydsm.husky.domain.application.domain.CalculatedScore;
import kr.hs.entrydsm.husky.domain.application.domain.GeneralApplication;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.CalculatedScoreRepository;
import kr.hs.entrydsm.husky.domain.application.domain.value.GradeScore;
import kr.hs.entrydsm.husky.domain.grade.exception.UserNotFoundException;
import kr.hs.entrydsm.husky.domain.grade.util.GradeUtil;
import kr.hs.entrydsm.husky.domain.grade.value.GradeMatrix;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.enums.ApplyType;
import kr.hs.entrydsm.husky.domain.user.domain.enums.GradeType;
import kr.hs.entrydsm.husky.domain.user.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.math.RoundingMode.*;
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
        User user = userRepository.findById(receiptCode)
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
        if (user.isGED()) {
            return DEFAULT_ATTENDANCE_SCORE;
        }

        GeneralApplication app = user.getGeneralApplication();

        int odmission = app.getLateCount() + app.getEarlyLeaveCount() + app.getPeriodCutCount();
        if (odmission != 0) odmission /= 3;

        int conversionAbsence = app.getFullCutCount() + odmission;
        return DEFAULT_ATTENDANCE_SCORE - conversionAbsence;

    }

    private BigDecimal calcVolunteerScore(User user) {
        if (user.isGED()) {
            return user.getGedApplication().getGedAverageScore()
                    .subtract(BigDecimal.valueOf(40))
                    .divide(BigDecimal.valueOf(5), 3, HALF_UP)
                    .add(BigDecimal.valueOf(3));
        }

        GeneralApplication app = user.getGeneralApplication();
        if (app.getVolunteerTime() >= 45) {
            return BigDecimal.valueOf(15);
        }

        if (app.getVolunteerTime() <= 9) {
            return BigDecimal.valueOf(3);
        }

        return BigDecimal.valueOf(app.getVolunteerTime())
                .subtract(BigDecimal.valueOf(9))
                .divide(BigDecimal.valueOf(3), 4, DOWN)
                .add(BigDecimal.valueOf(3))
                .setScale(3, HALF_UP);
    }

    private GradeScore calcGradeScore(User user) {
        GradeScore gradeScore;

        if (user.isGED()) {
            BigDecimal conversionScore = calcGEDConversionScore(user.getGedApplication().getGedAverageScore());
            gradeScore = new GradeScore(conversionScore);

        } else {
            gradeScore = this.calcGeneralApplication(user);
            if (gradeScore.isEmpty()) return gradeScore;
        }

        if (user.getApplyType() != ApplyType.COMMON) {
            gradeScore.multiplyAll(BigDecimal.valueOf(0.6));
        }

        gradeScore.setScaleAll(3, HALF_UP);

        if (user.getGradeType() != GradeType.GED) {
            gradeScore.setConversionScore();
        }

        return gradeScore;
    }

    private BigDecimal calcGEDConversionScore(BigDecimal averageScore) {
        return averageScore
                .subtract(BigDecimal.valueOf(50))
                .divide(BigDecimal.valueOf(50), 3, DOWN)
                .multiply(BigDecimal.valueOf(150))
                .setScale(3, HALF_UP);
    }

    private BigDecimal calcFinalScore(int attendanceScore, BigDecimal volunteerScore, GradeScore gradeScore) {
        return BigDecimal.valueOf(attendanceScore)
                .add(volunteerScore)
                .add(gradeScore.getConversionScore());
    }

    private GradeScore calcGeneralApplication(User user) {
        GeneralApplication application = user.getGeneralApplication();
        GradeMatrix matrix = new GradeMatrix(application);
        GradeUtil matrixUtil = new GradeUtil(user, matrix);

        BigDecimal firstGradeScore = null;
        BigDecimal secondGradeScore = null;
        BigDecimal thirdGradeScore;

        if (matrixUtil.isAllGradeEmpty())
            return GradeScore.EMPTY();

        boolean isFirstGradeEmpty = matrixUtil.isFirstGradeEmpty();
        boolean isSecondGradeEmpty = matrixUtil.isSecondGradeEmpty();

        matrixUtil.fillEmptySemester();

        if (isFirstGradeEmpty && isSecondGradeEmpty) {
            firstGradeScore = matrixUtil.getAverageScoreIf3rdGradeLeft();
            secondGradeScore = firstGradeScore;

        } else if (isFirstGradeEmpty && !isSecondGradeEmpty) {
            firstGradeScore = matrixUtil.getAverageScoreIf1stGradeEmpty();

        } else if (!isFirstGradeEmpty && isSecondGradeEmpty) {
            secondGradeScore = matrixUtil.getAverageScoreIf2ndGradeEmpty();
        }

        if (!isFirstGradeEmpty) {
            firstGradeScore = matrixUtil.getScore(SEMESTER_1_1, SEMESTER_1_2);
        }

        if (!isSecondGradeEmpty) {
            secondGradeScore = matrixUtil.getScore(SEMESTER_2_1, SEMESTER_2_2);
        }

        firstGradeScore = firstGradeScore.multiply(BigDecimal.valueOf(4.5)).setScale(6, DOWN);
        secondGradeScore = secondGradeScore.multiply(BigDecimal.valueOf(4.5)).setScale(6, DOWN);
        thirdGradeScore = matrixUtil.getScore(SEMESTER_3_1, SEMESTER_3_2)
                .multiply(SIX).setScale(6, DOWN);

        return GradeScore.builder()
                .firstGradeScore(firstGradeScore)
                .secondGradeScore(secondGradeScore)
                .thirdGradeScore(thirdGradeScore)
                .conversionScore(BigDecimal.ZERO)
                .build();
    }

}
