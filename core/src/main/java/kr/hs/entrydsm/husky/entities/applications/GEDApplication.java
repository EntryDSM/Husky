package kr.hs.entrydsm.husky.entities.applications;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Builder
@Entity(name = "ged_application")
@NoArgsConstructor
@AllArgsConstructor
public class GEDApplication extends Application {

    @Column
    private Integer gedAverageScore;

}
