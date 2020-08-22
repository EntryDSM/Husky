package kr.hs.entrydsm.husky.entities.applications;

import kr.hs.entrydsm.husky.entities.schools.School;
import kr.hs.entrydsm.husky.entities.users.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
public abstract class GeneralApplication extends Application {

    @Column(length = 5)
    private String studentNumber;

    @OneToOne
    @JoinColumn(name = "school_code")
    private School school;

    @Column(length = 20)
    private String schoolTel;

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

    GeneralApplication(Integer receiptCode, User user) {
        super(receiptCode, user);
    }

    public void setStudentInfo(String studentNumber, School school, String schoolTel) {
        this.studentNumber = studentNumber;
        this.school = school;
        this.schoolTel = schoolTel;
    }

    public void setScore(Integer volunteerTime, Integer fullCutCount, Integer periodCutCount, Integer lateCount,
                         Integer earlyLeaveCount, String korean, String social, String history, String math,
                         String science, String techAndHome, String english) {
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

    public boolean isFilledScore() {
        return volunteerTime != null && fullCutCount != null && periodCutCount != null && lateCount != null &&
                earlyLeaveCount != null && korean != null && social != null && history != null && math != null &&
                science != null && techAndHome != null && english != null;
    }

    public boolean isFilledStudentInfo() {
        return studentNumber != null && school != null && schoolTel != null;
    }

}
