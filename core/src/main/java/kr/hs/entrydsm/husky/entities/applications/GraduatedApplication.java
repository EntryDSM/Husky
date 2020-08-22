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
    public GraduatedApplication(Integer receiptCode, User user, LocalDate graduatedDate) {
        super(receiptCode, user);
        this.graduatedDate = graduatedDate;
    }

}
