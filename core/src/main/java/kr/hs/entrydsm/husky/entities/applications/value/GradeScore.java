package kr.hs.entrydsm.husky.entities.applications.value;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GradeScore {

    private BigDecimal firstGradeScore;

    private BigDecimal secondGradeScore;

    private BigDecimal thirdGradeScore;

    private BigDecimal conversionScore;

    public GradeScore(BigDecimal conversionScore) {
        this.firstGradeScore = BigDecimal.ZERO;
        this.secondGradeScore = BigDecimal.ZERO;
        this.thirdGradeScore = BigDecimal.ZERO;
        this.conversionScore = conversionScore;
    }

    @Builder
    public GradeScore(BigDecimal firstGradeScore, BigDecimal secondGradeScore, BigDecimal thirdGradeScore, BigDecimal conversionScore) {
        this.firstGradeScore = firstGradeScore;
        this.secondGradeScore = secondGradeScore;
        this.thirdGradeScore = thirdGradeScore;
        this.conversionScore = conversionScore;
    }

}
