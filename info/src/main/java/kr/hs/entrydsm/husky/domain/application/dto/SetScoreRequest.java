package kr.hs.entrydsm.husky.domain.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetScoreRequest {

    @NotNull @Min(0)
    private int volunteerTime;
    @NotNull @Min(0)
    private int fullCutCount;
    @NotNull @Min(0)
    private int periodCutCount;
    @NotNull @Min(0)
    private int lateCount;
    @NotNull @Min(0)
    private int earlyLeaveCount;
    @Pattern(regexp = "[AX][AX][AX][AX][AX]")
    private String korean;
    @Pattern(regexp = "[AX][AX][AX][AX][AX]")
    private String social;
    @Pattern(regexp = "[AX][AX][AX][AX][AX]")
    private String history;
    @Pattern(regexp = "[AX][AX][AX][AX][AX]")
    private String math;
    @Pattern(regexp = "[AX][AX][AX][AX][AX]")
    private String science;
    @Pattern(regexp = "[AX][AX][AX][AX][AX]")
    private String techAndHome;
    @Pattern(regexp = "[AX][AX][AX][AX][AX]")
    private String english;

}
