package kr.hs.entrydsm.husky.domain.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SetGedScoreRequest {
    @NotNull
    private int gedAverageScore;
}
