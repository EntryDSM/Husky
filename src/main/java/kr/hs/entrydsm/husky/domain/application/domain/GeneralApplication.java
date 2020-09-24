package kr.hs.entrydsm.husky.domain.application.domain;

import kr.hs.entrydsm.husky.domain.application.dto.SetScoreRequest;
import kr.hs.entrydsm.husky.domain.school.domain.School;
import kr.hs.entrydsm.husky.domain.user.dto.SetUserInfoRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.util.function.Consumer;

import static kr.hs.entrydsm.husky.global.util.Validator.isExists;

@Getter
@Setter(AccessLevel.PROTECTED)
@MappedSuperclass
public abstract class GeneralApplication extends BaseTimeEntity {

    @Column(length = 5)
    private String studentNumber;

    @OneToOne
    @JoinColumn(name = "school_code")
    private School school;

    @Column(length = 20)
    private String schoolTel;

    @Column
    private LocalDate graduatedDate;

    @Column
    private Integer volunteerTime;

    @Column
    private Integer fullCutCount;           // 미인정 결석 일수

    @Column
    private Integer periodCutCount;         // 미인정 결과 횟수

    @Column
    private Integer lateCount;              // 미인정 지각 횟수

    @Column
    private Integer earlyLeaveCount;        // 미인정 조퇴 횟수

    @Column(length = 6)
    private String korean;                  // 국어

    @Column(length = 6)
    private String social;                  // 사회

    @Column(length = 6)
    private String history;                 // 역사

    @Column(length = 6)
    private String math;                    // 수학

    @Column(length = 6)
    private String science;                 // 과학

    @Column(length = 6)
    private String techAndHome;             // 기술•가정

    @Column(length = 6)
    private String english;                 // 영어

    public void update(SetUserInfoRequest dto) {
        setIfNotNull(this::setStudentNumber, dto.getStudentNumber());
        setIfNotNull(this::setSchoolTel, dto.getSchoolTel());
    }

    public void update(School school) {
        setIfNotNull(this::setSchool, school);
    }

    protected void updateGraduatedDate(LocalDate graduatedDate) {
        this.graduatedDate = graduatedDate;
    }

    protected void updateVolunteerAndAttendance(SetScoreRequest dto) {
        setIfNotNull(this::setVolunteerTime, dto.getVolunteerTime());
        setIfNotNull(this::setFullCutCount, dto.getFullCutCount());
        setIfNotNull(this::setPeriodCutCount, dto.getPeriodCutCount());
        setIfNotNull(this::setLateCount, dto.getLateCount());
        setIfNotNull(this::setEarlyLeaveCount, dto.getEarlyLeaveCount());
    }

    protected void updateGrade(SetScoreRequest dto) {
        setIfNotNull(this::setKorean, dto.getKorean());
        setIfNotNull(this::setSocial, dto.getSocial());
        setIfNotNull(this::setHistory, dto.getHistory());
        setIfNotNull(this::setMath, dto.getMath());
        setIfNotNull(this::setScience, dto.getScience());
        setIfNotNull(this::setTechAndHome, dto.getTechAndHome());
        setIfNotNull(this::setEnglish, dto.getEnglish());
    }

    private <T> void setIfNotNull(Consumer<T> setter, T value) {
        if (value != null)
            setter.accept(value);
    }
    
    public boolean isFilledScore() {
        return volunteerTime != null && fullCutCount != null && periodCutCount != null && lateCount != null &&
                earlyLeaveCount != null && korean != null && social != null && history != null && math != null &&
                science != null && techAndHome != null && english != null;
    }

    public boolean isFilledStudentInfo() {
        return isExists(studentNumber) && school != null && isExists(schoolTel);
    }

    public boolean isSchoolEmpty() {
        return this.school == null;
    }

    public String getSchoolName() {
        return (isSchoolEmpty()) ? null : school.getSchoolName();
    }

    public String getSchoolCode() {
        return (isSchoolEmpty()) ? null : school.getSchoolCode();
    }

    public String getSchoolClass() {
        return (isExists(studentNumber)) ? studentNumber.substring(1, 3).replace("0", "") : null;
    }

    protected GeneralApplication() {}

    public GeneralApplication(String studentNumber, School school, String schoolTel, LocalDate graduatedDate,
                              Integer volunteerTime, Integer fullCutCount, Integer periodCutCount,
                              Integer lateCount, Integer earlyLeaveCount, String korean, String social,
                              String history, String math, String science, String techAndHome, String english) {
        this.studentNumber = studentNumber;
        this.school = school;
        this.schoolTel = schoolTel;
        this.graduatedDate = graduatedDate;
        this.volunteerTime = volunteerTime;
        this.fullCutCount = fullCutCount;
        this.periodCutCount = periodCutCount;
        this.lateCount = lateCount;
        this.earlyLeaveCount = earlyLeaveCount;
        this.korean = korean;
        this.social = social;
        this.history = history;
        this.math = math;
        this.science = science;
        this.techAndHome = techAndHome;
        this.english = english;
    }

}
