package kr.hs.entrydsm.husky.entities.applications;

import kr.hs.entrydsm.husky.entities.users.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Getter
@Entity(name = "ged_application")
@NoArgsConstructor
@AllArgsConstructor
public class GEDApplication extends Application {

    @Digits(integer = 3, fraction = 2)
    private BigDecimal gedAverageScore;

    @Builder(builderMethodName = "gedApplicationBuilder")
    public GEDApplication(User user, BigDecimal gedAverageScore) {
        super(user);
        this.gedAverageScore = gedAverageScore;
    }

}
