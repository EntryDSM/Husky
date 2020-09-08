package kr.hs.entrydsm.husky.domain.user.domain;

import kr.hs.entrydsm.husky.domain.application.domain.*;
import kr.hs.entrydsm.husky.domain.user.domain.enums.AdditionalType;
import kr.hs.entrydsm.husky.domain.user.domain.enums.ApplyType;
import kr.hs.entrydsm.husky.domain.user.domain.enums.GradeType;
import kr.hs.entrydsm.husky.domain.user.domain.enums.Sex;
import kr.hs.entrydsm.husky.domain.user.dto.SelectTypeRequest;
import kr.hs.entrydsm.husky.domain.user.dto.SetUserInfoRequest;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Consumer;

import static kr.hs.entrydsm.husky.domain.user.domain.enums.GradeType.*;

@Getter
@Setter
@Builder
@Entity(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity {

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
    private Boolean isDaejeon;

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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Status status;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private GEDApplication gedApplication;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private GraduatedApplication graduatedApplication;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UnGraduatedApplication unGraduatedApplication;

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

    public void updateClassification(SelectTypeRequest dto) {
        setIfNotNull(this::setGradeType, dto.getGradeType());
        setIfNotNull(this::setApplyType, dto.getApplyType());
        setIfNotNull(this::setAdditionalType, dto.getAdditionalType());
        setIfNotNull(this::setIsDaejeon, dto.getIsDaejeon());
    }

    public void updateInfo(SetUserInfoRequest dto) {
        setIfNotNull(this::setName, dto.getName());
        setIfNotNull(this::setSex, dto.getSex());
        setIfNotNull(this::setBirthDate, dto.getBirthDate());
        setIfNotNull(this::setParentName, dto.getParentName());
        setIfNotNull(this::setParentTel, dto.getParentTel());
        setIfNotNull(this::setApplicantTel, dto.getApplicantTel());
        setIfNotNull(this::setHomeTel, dto.getHomeTel());
        setIfNotNull(this::setAddress, dto.getAddress());
        setIfNotNull(this::setDetailAddress, dto.getDetailAddress());
        setIfNotNull(this::setPostCode, dto.getPostCode());
        this.userPhoto = dto.getPhoto();
    }

    private <T> void setIfNotNull(final Consumer<T> setter, final T value) {
        if (value != null)
            setter.accept(value);
    }

    public void setSelfIntroduction(String introduction) {
        this.selfIntroduction = introduction;
    }

    public void setStudyPlan(String plan) {
        this.studyPlan = plan;
    }

    public boolean isGED() {
        return gradeType == GED;
    }

    public boolean isGraduated() {
        return gradeType == GRADUATED;
    }

    public boolean isUngraduated() {
        return gradeType == UNGRADUATED;
    }

    public boolean isFilledInfo() {
        return name != null && sex != null && birthDate != null && applicantTel != null && parentTel != null &&
                parentName != null && address != null && detailAddress != null && postCode != null && userPhoto != null;
    }

    public boolean isFilledType() {
        return gradeType != null && applyType != null && additionalType != null && hasApplication();
    }

    private boolean hasApplication() {
        if (gradeType == null)
            return false;

        if (gradeType.equals(GED))
            return gedApplication != null;

        return getGeneralApplication() != null;
    }

    public boolean isMale() {
        return this.sex.equals(Sex.MALE);
    }

    public boolean isFemale() {
        return this.sex.equals(Sex.FEMALE);
    }

    public boolean isGradeTypeEmpty() {
        return this.gradeType == null;
    }

    public boolean isPhotoEmpty() {
        return this.userPhoto.isEmpty();
    }

}
