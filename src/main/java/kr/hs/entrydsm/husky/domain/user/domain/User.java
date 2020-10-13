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
import java.util.function.Consumer;

import static kr.hs.entrydsm.husky.domain.user.domain.enums.ApplyType.COMMON;
import static kr.hs.entrydsm.husky.domain.user.domain.enums.ApplyType.MEISTER;
import static kr.hs.entrydsm.husky.domain.user.domain.enums.GradeType.*;
import static kr.hs.entrydsm.husky.global.util.Validator.isExists;

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
        this.homeTel = dto.getHomeTel();
        setIfNotNull(this::setAddress, dto.getAddress());
        setIfNotNull(this::setDetailAddress, dto.getDetailAddress());
        setIfNotNull(this::setPostCode, dto.getPostCode());
        setIfNotNull(this::setUserPhoto, dto.getPhoto());
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
        return isExists(name) && sex != null && birthDate != null && isExists(applicantTel) && isExists(parentTel) &&
                isExists(parentName) && isExists(address) && isExists(detailAddress) && isExists(postCode) &&
                userPhoto != null;
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
        return this.userPhoto == null;
    }

    public boolean isHomeTelEmpty() {
        return this.homeTel == null || this.homeTel.isBlank();
    }

    public boolean isAdditionalTypeEquals(AdditionalType type) {
        return (additionalType != null) && additionalType.equals(type);
    }

    public boolean isApplyTypeEmpty() {
        return this.applyType == null;
    }

    public boolean isCommonApplyType() {
        return !isApplyTypeEmpty() && applyType.equals(COMMON);
    }

    public boolean isMeisterApplyType() {
        return !isApplyTypeEmpty() && applyType.equals(MEISTER);
    }

    public boolean isSocialMeritApplyType() {
        return !isCommonApplyType() && !isMeisterApplyType();
    }

}
