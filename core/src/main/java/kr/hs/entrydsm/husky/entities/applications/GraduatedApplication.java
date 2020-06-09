package kr.hs.entrydsm.husky.entities.applications;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Getter
@Builder
@Entity
@NoArgsConstructor
public class GraduatedApplication extends GeneralApplication {
}
