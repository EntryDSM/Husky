package kr.hs.entrydsm.husky.domain.grade.value;

import kr.hs.entrydsm.husky.domain.application.domain.GeneralApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GradeMatrix {
    
    private List<List<Integer>> matrix;

    public GradeMatrix(GeneralApplication application) {
        List<String> grades = new ArrayList<>();
        grades.add(application.getKorean());
        grades.add(application.getSocial());
        grades.add(application.getHistory());
        grades.add(application.getMath());
        grades.add(application.getScience());
        grades.add(application.getTechAndHome());
        grades.add(application.getEnglish());

        this.matrix = grades.stream()
                .map(this::convertToGradeScores)
                .collect(Collectors.toList());
    }

    private List<Integer> convertToGradeScores(String gradeString) {
        return Arrays.stream(gradeString.split(""))
                .map(this::convertToInteger)
                .collect(Collectors.toList());
    }

    private Integer convertToInteger(String singleGradeString) {
        switch (singleGradeString) {
            case "A":
                return 5;
            case "B":
                return 4;
            case "C":
                return 3;
            case "D":
                return 2;
            case "E":
                return 1;
            default:
                return 0;
        }
    }

    public List<List<Integer>> getMatrix() {
        return matrix;
    }

}
