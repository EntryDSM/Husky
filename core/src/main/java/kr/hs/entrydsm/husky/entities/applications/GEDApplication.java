package kr.hs.entrydsm.husky.entities.applications;

import kr.hs.entrydsm.husky.entities.users.User;
import lombok.*;

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

    @Builder(builderMethodName = "gedApplicationBuilder")
    public GEDApplication(User user, BigDecimal gedAverageScore) {
        super(user);
        this.gedAverageScore = gedAverageScore;
    }

    @Column
    private LocalDate gedPassDate;

    @Builder
    public GEDApplication(LocalDate gedPassDate, String email, User user) {
        super(email, user);
        this.gedPassDate = gedPassDate;
    }

    public void setGedAverageScore(Integer gedAverageScore) {
        this.gedAverageScore = gedAverageScore;
    }
}
