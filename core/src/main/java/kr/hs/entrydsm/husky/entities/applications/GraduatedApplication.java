package kr.hs.entrydsm.husky.entities.applications;

import kr.hs.entrydsm.husky.entities.users.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
public class GraduatedApplication extends GeneralApplication {

    @Column
    private LocalDate graduatedDate;

    @Builder
    public GraduatedApplication(String email, User user, LocalDate graduatedDate) {
        super(email, user);
        this.graduatedDate = graduatedDate;
    }

}
