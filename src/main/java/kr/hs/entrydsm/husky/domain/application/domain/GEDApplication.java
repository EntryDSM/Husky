package kr.hs.entrydsm.husky.domain.application.domain;

import kr.hs.entrydsm.husky.domain.user.domain.User;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Entity(name = "ged_application")
@NoArgsConstructor
public class GEDApplication extends Application {

    @Digits(integer = 3, fraction = 2)
    private BigDecimal gedAverageScore;

    @Column
    private LocalDate gedPassDate;

    public void setGedAverageScore(BigDecimal gedAverageScore) {
        this.gedAverageScore = gedAverageScore;
    }

    @Builder(builderMethodName = "gedApplicationBuilder")
    public GEDApplication(User user, BigDecimal gedAverageScore, LocalDate gedPassDate) {
        super(user);
        this.gedAverageScore = gedAverageScore;
        this.gedPassDate = gedPassDate;
    }

}
