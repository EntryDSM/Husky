package kr.hs.entrydsm.husky.domain.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.hs.entrydsm.husky.entities.applications.GEDApplication;
import kr.hs.entrydsm.husky.entities.applications.GeneralApplication;
import kr.hs.entrydsm.husky.entities.users.enums.GradeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ScoreResponse {

    private GradeType gradeType;

    private BigDecimal gedAverageScore;

    private int volunteerTime;
    private int fullCutCount;
    private int periodCutCount;
    private int lateCount;
    private int earlyLeaveCount;
    private String korean;
    private String social;
    private String history;
    private String math;
    private String science;
    private String techAndHome;
    private String english;

    public static ScoreResponse gedResponse(GEDApplication gedApplication, GradeType gradeType) {
        return ScoreResponse.builder()
                .gradeType(gradeType)
                .gedAverageScore(gedApplication.getGedAverageScore())
                .build();
    }

    public static ScoreResponse response(GeneralApplication application, GradeType gradeType) {
        return ScoreResponse.builder()
                .gradeType(gradeType)
                .volunteerTime(application.getVolunteerTime())
                .fullCutCount(application.getFullCutCount())
                .periodCutCount(application.getPeriodCutCount())
                .lateCount(application.getLateCount())
                .earlyLeaveCount(application.getEarlyLeaveCount())
                .korean(application.getKorean())
                .social(application.getSocial())
                .history(application.getHistory())
                .math(application.getMath())
                .science(application.getScience())
                .techAndHome(application.getTechAndHome())
                .english(application.getEnglish())
                .build();
    }

}
