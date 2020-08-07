package kr.hs.entrydsm.husky.domain.user.dto;

import kr.hs.entrydsm.husky.entities.users.enums.GradeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGradeResponse {

    private GradeType gradeType;
    private int gedAverageScore;

}
