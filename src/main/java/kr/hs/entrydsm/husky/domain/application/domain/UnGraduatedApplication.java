package kr.hs.entrydsm.husky.domain.application.domain;

import kr.hs.entrydsm.husky.domain.application.dto.SetScoreRequest;
import kr.hs.entrydsm.husky.domain.school.domain.School;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity(name = "ungraduated_application")
@NoArgsConstructor
public class UnGraduatedApplication extends GeneralApplication {

    @Id
    @Column(name = "user_receipt_code")
    private Integer receiptCode;

    @OneToOne
    @PrimaryKeyJoinColumn
    private User user;

    public UnGraduatedApplication update(SetScoreRequest dto) {
        updateVolunteerAndAttendance(dto);
        updateGrade(dto);
        return this;
    }

    public UnGraduatedApplication(Integer receiptCode) {
        this.receiptCode = receiptCode;
    }

    @Builder(builderMethodName = "unGraduatedApplicationBuilder")
    public UnGraduatedApplication(Integer receiptCode, String studentNumber, School school, String schoolTel, Integer volunteerTime, Integer fullCutCount, Integer periodCutCount, Integer lateCount, Integer earlyLeaveCount, String korean, String social, String history, String math, String science, String techAndHome, String english) {
        super(studentNumber, school, schoolTel, volunteerTime, fullCutCount, periodCutCount, lateCount, earlyLeaveCount, korean, social, history, math, science, techAndHome, english);
        this.receiptCode = receiptCode;
    }

}
