package kr.hs.entrydsm.husky.entities.applications;

import kr.hs.entrydsm.husky.entities.users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Application extends BaseTimeEntity{

    @Id
    @Column(name = "user_email")
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private User user;

}
