package kr.hs.entrydsm.husky.domain.application.domain.value;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
public class GradeScore {

    private BigDecimal firstGradeScore;

    private BigDecimal secondGradeScore;

    private BigDecimal thirdGradeScore;

    private BigDecimal conversionScore;

    public void multiplyAll(BigDecimal multiplicand) {
        this.firstGradeScore = firstGradeScore.multiply(multiplicand);
        this.secondGradeScore = secondGradeScore.multiply(multiplicand);
        this.thirdGradeScore = thirdGradeScore.multiply(multiplicand);
        this.conversionScore = conversionScore.multiply(multiplicand);
    }

    public void setScaleAll(int newScale, RoundingMode roundingMode) {
        this.firstGradeScore = firstGradeScore.setScale(newScale, roundingMode);
        this.secondGradeScore = secondGradeScore.setScale(newScale, roundingMode);
        this.thirdGradeScore = thirdGradeScore.setScale(newScale, roundingMode);
        this.conversionScore = conversionScore.setScale(newScale, roundingMode);
    }

    public void setConversionScore() {
        this.conversionScore = firstGradeScore
                .add(secondGradeScore)
                .add(thirdGradeScore);
    }

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

    public static GradeScore EMPTY() {
        return GradeScore.builder()
                .firstGradeScore(BigDecimal.ZERO)
                .secondGradeScore(BigDecimal.ZERO)
                .thirdGradeScore(BigDecimal.ZERO)
                .conversionScore(BigDecimal.ZERO)
                .build();
    }

    public boolean isEmpty() {
        return firstGradeScore.equals(BigDecimal.ZERO)
                && secondGradeScore.equals(BigDecimal.ZERO)
                && thirdGradeScore.equals(BigDecimal.ZERO)
                && conversionScore.equals(BigDecimal.ZERO);
    }

}
