package kr.hs.entrydsm.husky.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddScoreRequest {

    @NotNull
    private int volunteerTime;
    @NotNull
    private int fullCutCount;
    @NotNull
    private int periodCutCount;
    @NotNull
    private int lateCount;
    @NotNull
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
