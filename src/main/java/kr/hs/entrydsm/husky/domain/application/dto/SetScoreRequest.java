package kr.hs.entrydsm.husky.domain.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetScoreRequest {

    @PositiveOrZero
    private int volunteerTime;
    
    @PositiveOrZero
    private int fullCutCount;
    
    @PositiveOrZero
    private int periodCutCount;
    
    @PositiveOrZero
    private int lateCount;
    
    @PositiveOrZero
    private int earlyLeaveCount;
    
    @Pattern(regexp = "[A-E,X]{6}")
    private String korean;
    
    @Pattern(regexp = "[A-E,X]{6}")
    private String social;
    
    @Pattern(regexp = "[A-E,X]{6}")
    private String history;
    
    @Pattern(regexp = "[A-E,X]{6}")
    private String math;
    
    @Pattern(regexp = "[A-E,X]{6}")
    private String science;
    
    @Pattern(regexp = "[A-E,X]{6}")
    private String techAndHome;
    
    @Pattern(regexp = "[A-E,X]{6}")
    private String english;

}
