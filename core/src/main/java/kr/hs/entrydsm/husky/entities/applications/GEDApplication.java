package kr.hs.entrydsm.husky.entities.applications;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Builder
@Entity(name = "ged_application")
@NoArgsConstructor
@AllArgsConstructor
public class GEDApplication extends Application {

    @Column
    private Integer gedAverageScore;

}
