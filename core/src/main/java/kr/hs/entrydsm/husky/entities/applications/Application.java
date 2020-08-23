package kr.hs.entrydsm.husky.entities.applications;

import kr.hs.entrydsm.husky.entities.users.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Application extends BaseTimeEntity {

    @Id
    @Column
    private Integer receiptCode;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @Builder(builderMethodName = "applicationBuilder")
    public Application(User user) {
        this.receiptCode = user.getReceiptCode();
        this.user = user;
    }

}
