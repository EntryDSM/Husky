package kr.hs.entrydsm.husky.entities.users;

import kr.hs.entrydsm.husky.entities.applications.*;
import kr.hs.entrydsm.husky.entities.users.enums.AdditionalType;
import kr.hs.entrydsm.husky.entities.users.enums.ApplyType;
import kr.hs.entrydsm.husky.entities.users.enums.GradeType;
import kr.hs.entrydsm.husky.entities.users.enums.Sex;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Builder
@Entity(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer receiptCode;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

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
    private LocalDate birthDate;

    @Column(length = 15)
    private String parentName;

    @Column(length = 20)
    private String parentTel;

    @Column(length = 20)
    private String applicantTel;

    @Column(length = 250)
    private String address;

    @Column(length = 250)
    private String detailAddress;

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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private GEDApplication gedApplication;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private GraduatedApplication graduatedApplication;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UnGraduatedApplication unGraduatedApplication;

    public Application getApplication() {
        switch (gradeType) {
            case GED:
                return this.gedApplication;
            case GRADUATED:
                return this.graduatedApplication;
            case UNGRADUATED:
                return this.unGraduatedApplication;
            default:
                return null;
        }
    }

    public GeneralApplication getGeneralApplication() {
        switch (gradeType) {
            case GRADUATED:
                return this.graduatedApplication;
            case UNGRADUATED:
                return this.unGraduatedApplication;
            default:
                return null;
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setClassification(GradeType gradeType, ApplyType applyType, AdditionalType additionalType,
                                  boolean isDaejeon) {
        this.gradeType = gradeType;
        this.applyType = applyType;
        this.additionalType = additionalType;
        this.isDaejeon = isDaejeon;
    }


    public void setInfo(String name, Sex sex, LocalDate birthDate, String applicantTel, String parentTel,
                        String parentName, String address, String detailAddress, String postCode, String photo) {
        this.name = name;
        this.sex = sex;
        this.birthDate = birthDate;
        this.applicantTel = applicantTel;
        this.parentTel = parentTel;
        this.parentName = parentName;
        this.address = address;
        this.detailAddress = detailAddress;
        this.postCode = postCode;
        this.userPhoto = photo;
    }

    public void setSelfIntroduction(String introduction) {
        this.selfIntroduction = introduction;
    }

    public void setStudyPlan(String plan) {
        this.studyPlan = plan;
    }

    public boolean isGed() {
        return gradeType == GradeType.GED;
    }

    public boolean isGraduated() {
        return gradeType == GradeType.GRADUATED;
    }

    public boolean isUngraduated() {
        return gradeType == GradeType.UNGRADUATED;
    }

    public boolean isSetInfo() {
        return name != null && sex != null && birthDate != null && applicantTel != null && parentTel != null &&
                parentName != null && address != null && detailAddress != null && postCode != null && userPhoto != null;
    }

    public boolean isSetType() {
        return gradeType != null && applyType != null && additionalType != null && getApplication() != null;
    }

}
