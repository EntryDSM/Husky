package kr.hs.entrydsm.husky.entities.applications;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@Builder
@Entity(name = "ungraduated_application")
@NoArgsConstructor
public class UnGraduatedApplication extends GeneralApplication {

}
