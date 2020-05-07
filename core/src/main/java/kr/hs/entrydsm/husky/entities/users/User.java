package kr.hs.entrydsm.husky.entities.users;

import kr.hs.entrydsm.husky.entities.applications.Application;
import kr.hs.entrydsm.husky.entities.applications.GEDApplication;
import kr.hs.entrydsm.husky.entities.applications.GraduatedApplication;
import kr.hs.entrydsm.husky.entities.applications.UnGraduatedApplication;
import kr.hs.entrydsm.husky.entities.users.enums.AdditionalType;
import kr.hs.entrydsm.husky.entities.users.enums.ApplyType;
import kr.hs.entrydsm.husky.entities.users.enums.GradeType;
import kr.hs.entrydsm.husky.entities.users.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@Entity(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(nullable = false)
    private Integer receiptNumber;

    @Enumerated(EnumType.STRING)
    private ApplyType applyType;

    @Enumerated(EnumType.STRING)
    private AdditionalType additionalType;

    @Enumerated(EnumType.STRING)
    private GradeType gradeType;

    @Column
    private boolean isDaejeon;

    @Column(length = 15)
    private String name;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column
    private Date birthDate;

    @Column(length = 15)
    private String parentName;

    @Column(length = 20)
    private String parentTel;

    @Column(length = 20)
    private String applicantTel;

    @Column(length = 500)
    private String address;

    @Column(length = 5)
    private String postCode;

    @Column(length = 45)
    private String userPhoto;

    @Column(length = 45)
    private String homeTel;

    @Column(length = 1600)
    private String selfIntroduction;

    @Column(length = 1600)
    private String studyPlan;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user")
    private Status status;

    @OneToOne(mappedBy = "user")
    private GEDApplication gedApplication;

    @OneToOne(mappedBy = "user")
    private GraduatedApplication graduatedApplication;

    @OneToOne(mappedBy = "user")
    private UnGraduatedApplication unGraduatedApplication;

    public Application getApplication() {
        switch (gradeType) {
            case GED:
                return this.gedApplication;
            case GRADUATED:
                return this.graduatedApplication;
            case UN_GRADUATED:
                return this.unGraduatedApplication;
            default:
                return null;
        }
    }

}