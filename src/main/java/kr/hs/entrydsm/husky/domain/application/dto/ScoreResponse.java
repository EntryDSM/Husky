package kr.hs.entrydsm.husky.domain.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.hs.entrydsm.husky.domain.application.domain.GEDApplication;
import kr.hs.entrydsm.husky.domain.application.domain.GeneralApplication;
import kr.hs.entrydsm.husky.domain.application.domain.adapter.GeneralApplicationAdapter;
import kr.hs.entrydsm.husky.domain.user.domain.enums.GradeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ScoreResponse {

    private GradeType gradeType;

    private BigDecimal gedAverageScore;

    private Integer volunteerTime;
    private Integer fullCutCount;
    private Integer periodCutCount;
    private Integer lateCount;
    private Integer earlyLeaveCount;
    private String korean;
    private String social;
    private String history;
    private String math;
    private String science;
    private String techAndHome;
    private String english;

    public ScoreResponse(GEDApplication gedApplication) {
        this.gradeType = GradeType.GED;
        this.gedAverageScore = gedApplication.getGedAverageScore();
    }

    public ScoreResponse(GeneralApplicationAdapter adapter) {
        GeneralApplication application = adapter.getGeneralApplication();
        this.gradeType = adapter.getGradeType();
        this.volunteerTime = application.getVolunteerTime();
        this.fullCutCount = application.getFullCutCount();
        this.periodCutCount = application.getPeriodCutCount();
        this.lateCount = application.getLateCount();
        this.earlyLeaveCount = application.getEarlyLeaveCount();
        this.korean = application.getKorean();
        this.social = application.getSocial();
        this.history = application.getHistory();
        this.math = application.getMath();
        this.science = application.getScience();
        this.techAndHome = application.getTechAndHome();
        this.english = application.getEnglish();
    }

}
