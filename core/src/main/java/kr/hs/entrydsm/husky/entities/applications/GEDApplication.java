package kr.hs.entrydsm.husky.entities.applications;

import kr.hs.entrydsm.husky.entities.users.User;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;

@Getter
@Entity(name = "ged_application")
@NoArgsConstructor
public class GEDApplication extends Application {

    @Column
    private Integer gedAverageScore;

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
