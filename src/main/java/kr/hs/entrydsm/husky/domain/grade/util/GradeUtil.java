package kr.hs.entrydsm.husky.domain.grade.util;

import kr.hs.entrydsm.husky.domain.grade.value.GradeMatrix;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.enums.GradeType;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static java.math.RoundingMode.*;
import static kr.hs.entrydsm.husky.domain.grade.constant.BigDecimalConstants.FOUR;
import static kr.hs.entrydsm.husky.domain.grade.constant.BigDecimalConstants.TWO;
import static kr.hs.entrydsm.husky.domain.grade.constant.Semester.*;

public class GradeUtil {

    private final User user;
    private final GradeMatrix gradeMatrix;

    public GradeUtil(User user, GradeMatrix gradeMatrix) {
        this.user = user;
        this.gradeMatrix = gradeMatrix;
    }

    public boolean isFirstGradeEmpty() {
        return isEmptyGrade(SEMESTER_1_1, SEMESTER_1_2);
    }

    public boolean isSecondGradeEmpty() {
        return isEmptyGrade(SEMESTER_2_1, SEMESTER_2_2);
    }

    public boolean isThirdGradeEmpty(GradeType gradeType) {
        switch (gradeType) {
            case UNGRADUATED:
                return isEmptyGrade(SEMESTER_3_1);

            case GRADUATED:
                return isEmptyGrade(SEMESTER_3_1, SEMESTER_3_2);

            default:
                return true;
        }

    }

    public boolean isAllGradeEmpty() {
        return isEmptyGrade(SEMESTER_1_1, SEMESTER_3_2);
    }

    private boolean isEmptyGrade(int beginColumn, int endColumn) {
        int sum = 0;
        for (int column = beginColumn; column <= endColumn; column++) {
            for (List<Integer> integers : gradeMatrix.getMatrix()) {
                sum += integers.get(column);
            }
        }

        return sum == 0;
    }

    private boolean isEmptyGrade(int semeseter) {
        int sum = 0;
        for (List<Integer> integers : gradeMatrix.getMatrix()) {
            sum += integers.get(semeseter);
        }

        return sum == 0;
    }

    public BigDecimal getAverageScoreIf3rdGradeLeft() {
        switch (user.getGradeType()) {
            case UNGRADUATED:
                return getScore(SEMESTER_3_1)
                        .multiply(TWO);

            case GRADUATED:
                return getScore(SEMESTER_3_1, SEMESTER_3_2);

            default:
                return BigDecimal.ZERO;
        }
    }

    public BigDecimal getAverageScoreIf1stGradeEmpty() {
        switch (user.getGradeType()) {
            case UNGRADUATED:
                return getScore(SEMESTER_2_1, SEMESTER_2_2)
                        .add(getScore(SEMESTER_3_1).multiply(TWO))
                        .multiply(TWO)
                        .divide(FOUR, 5, DOWN);

            case GRADUATED:
                return getScore(SEMESTER_2_1, SEMESTER_3_2)
                        .multiply(TWO)
                        .divide(FOUR, 5, DOWN);

            default:
                return BigDecimal.ZERO;
        }
    }

    public BigDecimal getAverageScoreIf2ndGradeEmpty() {
        switch (user.getGradeType()) {
            case UNGRADUATED:
                return getScore(SEMESTER_1_1, SEMESTER_1_2)
                        .add(getScore(SEMESTER_3_1).multiply(TWO))
                        .multiply(TWO)
                        .divide(FOUR, 5, DOWN);

            case GRADUATED:
                return getScore(SEMESTER_1_1, SEMESTER_1_2)
                        .add(getScore(SEMESTER_3_1, SEMESTER_3_2))
                        .multiply(TWO)
                        .divide(FOUR, 5, DOWN);

            default:
                return BigDecimal.ZERO;
        }
    }

    public BigDecimal getScore(int column) {
        int subject = 7;
        int sum = 0;

        List<List<Integer>> matrix = gradeMatrix.getMatrix();
        for (int row = 0; row <= matrix.size() - 1; row++) {
            int score = matrix.get(row).get(column);
            if (score == 0) subject--;
            else sum += score;
        }

        return BigDecimal.valueOf(sum)
                .divide(BigDecimal.valueOf(subject), 5, DOWN);
    }

    public BigDecimal getScore(int beginColumn, int endColumn) {
        BigDecimal sum = BigDecimal.ZERO;
        for (int index = beginColumn; index <= endColumn; index++) {
            sum = sum.add(getScore(index));
        }
        return sum;
    }

    public void fillEmptySemester() {
        fillNextSemester(SEMESTER_1_1, SEMESTER_1_2);
        fillNextSemester(SEMESTER_2_1, SEMESTER_2_2);
        fillNextSemester(SEMESTER_3_1, SEMESTER_3_2);
    }

    private void fillNextSemester(int first, int second) {
        boolean isFirstSemesterEmpty = isEmptySemester(first);
        boolean isSecondSemesterEmpty = isEmptySemester(second);

        if (isFirstSemesterEmpty && !isSecondSemesterEmpty) {
            copyToNextColumn(getSemester(second), first);
        } else if (!isFirstSemesterEmpty && isSecondSemesterEmpty) {
            copyToNextColumn(getSemester(first), second);
        }

    }

    private boolean isEmptySemester(int semester) {
        int sum = gradeMatrix.getMatrix().stream()
                .mapToInt(row -> row.get(semester)).sum();
        return sum == 0;
    }

    private List<Integer> getSemester(int semester) {
        return gradeMatrix.getMatrix().stream()
                .map(row -> row.get(semester))
                .collect(Collectors.toList());
    }

    private void copyToNextColumn(List<Integer> semesterGrades, int nextIndex) {
        for (int index = 0; index < semesterGrades.size(); index++) {
            gradeMatrix.getMatrix().get(index)
                    .set(nextIndex, semesterGrades.get(index));
        }
    }

}
