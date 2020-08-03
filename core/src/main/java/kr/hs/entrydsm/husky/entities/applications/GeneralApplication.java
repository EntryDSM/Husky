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

    GeneralApplication(String email, User user) {
        super(email, user);
    }

    public void setStudentInfo(String studentNumber, School school, String schoolTel) {
        this.studentNumber = studentNumber;
        this.school = school;
        this.schoolTel = schoolTel;
    }

}
