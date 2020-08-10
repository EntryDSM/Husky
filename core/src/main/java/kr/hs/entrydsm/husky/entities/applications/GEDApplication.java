package kr.hs.entrydsm.husky.entities.applications;

import kr.hs.entrydsm.husky.entities.users.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Entity(name = "ged_application")
@NoArgsConstructor
@AllArgsConstructor
public class GEDApplication extends Application {

    @Column
    private Integer gedAverageScore;

    @Builder(builderMethodName = "gedApplicationBuilder")
    public GEDApplication(User user, int gedAverageScore) {
        super(user);
        this.gedAverageScore = gedAverageScore;
    }

}
