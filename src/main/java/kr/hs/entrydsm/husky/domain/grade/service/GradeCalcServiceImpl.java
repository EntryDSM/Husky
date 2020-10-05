package kr.hs.entrydsm.husky.domain.grade.service;

import kr.hs.entrydsm.husky.domain.application.domain.CalculatedScore;
import kr.hs.entrydsm.husky.domain.application.domain.GeneralApplication;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.CalculatedScoreRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GEDApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GeneralApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.value.GradeScore;
import kr.hs.entrydsm.husky.domain.grade.util.GradeUtil;
import kr.hs.entrydsm.husky.domain.grade.value.GradeMatrix;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.enums.ApplyType;
import kr.hs.entrydsm.husky.domain.user.domain.enums.GradeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.math.RoundingMode.DOWN;
import static java.math.RoundingMode.HALF_UP;
import static kr.hs.entrydsm.husky.domain.grade.constant.BigDecimalConstants.SIX;
import static kr.hs.entrydsm.husky.domain.grade.constant.Semester.*;

@Service
@RequiredArgsConstructor
public class GradeCalcServiceImpl implements GradeCalcService {

    public static final int DEFAULT_ATTENDANCE_SCORE = 15;

    private final GEDApplicationRepository gedApplicationRepository;
    private final CalculatedScoreRepository calculatedScoreRepository;
    private final GeneralApplicationRepository generalApplicationRepository;

    @Override
    public CalculatedScore calcStudentGrade(User user) {
        if (user.isGradeTypeEmpty())
            return CalculatedScore.EMPTY(user);

        GeneralApplication generalApplication = generalApplicationRepository.findByUser(user);
        int attendanceScore = calcAttendanceScore(user, generalApplication);
        BigDecimal volunteerScore = calcVolunteerScore(user, generalApplication);
        GradeScore gradeScore = calcGradeScore(user, generalApplication);
        BigDecimal finalScore = calcFinalScore(attendanceScore, volunteerScore, gradeScore);

        return calculatedScoreRepository.save(CalculatedScore.builder()
                .user(user)
                .attendanceScore(attendanceScore)
                .volunteerScore(volunteerScore)
                .gradeScore(gradeScore)
                .finalScore(finalScore)
                .build());
    }

    private Integer calcAttendanceScore(User user, GeneralApplication generalApplication) {
        if (user.isGED())
            return DEFAULT_ATTENDANCE_SCORE;

        if (generalApplication == null) {
            return 0;
        }

        try {
            int odmission = generalApplication.getLateCount()
                    + generalApplication.getEarlyLeaveCount() + generalApplication.getPeriodCutCount();

            if (odmission != 0) odmission /= 3;
            int conversionAbsence = generalApplication.getFullCutCount() + odmission;
            return DEFAULT_ATTENDANCE_SCORE - conversionAbsence;

        } catch (NullPointerException e) {
            return 0;
        }
    }

    private BigDecimal calcVolunteerScore(User user, GeneralApplication generalApplication) {
        if (user.isGED()) {
            return gedApplicationRepository.findById(user.getReceiptCode())
                    .map(gedApplication -> gedApplication.getGedAverageScore()
                            .subtract(BigDecimal.valueOf(40))
                            .divide(BigDecimal.valueOf(5), 3, HALF_UP)
                            .add(BigDecimal.valueOf(3)))
                    .orElse(BigDecimal.ZERO);
        }

        if (generalApplication == null) {
            return BigDecimal.ZERO;
        }

        if (generalApplication.getVolunteerTime() >= 45) {
            return BigDecimal.valueOf(15);
        }

        if (generalApplication.getVolunteerTime() <= 9) {
            return BigDecimal.valueOf(3);
        }

        return BigDecimal.valueOf(generalApplication.getVolunteerTime())
                .subtract(BigDecimal.valueOf(9))
                .divide(BigDecimal.valueOf(3), 4, DOWN)
                .add(BigDecimal.valueOf(3))
                .setScale(3, HALF_UP);
    }

    private GradeScore calcGradeScore(User user, GeneralApplication generalApplication) {
        GradeScore gradeScore;

        if (user.isGED()) {
            gradeScore = gedApplicationRepository.findById(user.getReceiptCode())
                    .map(gedApplication -> {
                        BigDecimal conversionScore = calcGEDConversionScore(gedApplication.getGedAverageScore());
                        return new GradeScore(conversionScore);
                    })
                    .orElse(GradeScore.EMPTY());

        } else {
            gradeScore = this.calcGeneralApplication(user, generalApplication);
        }

        if (gradeScore.isEmpty()) {
            return gradeScore;
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

    private GradeScore calcGeneralApplication(User user, GeneralApplication generalApplication) {
        if (generalApplication == null)
            return GradeScore.EMPTY();

        GradeMatrix matrix = new GradeMatrix(generalApplication);
        GradeUtil matrixUtil = new GradeUtil(user, matrix);

        BigDecimal firstGradeScore = null;
        BigDecimal secondGradeScore = null;
        BigDecimal thirdGradeScore;

        if (matrixUtil.isAllGradeEmpty() || matrixUtil.isThirdGradeEmpty(user.getGradeType()))
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
