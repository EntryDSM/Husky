package kr.hs.entrydsm.husky.domain.application.domain;

import kr.hs.entrydsm.husky.domain.application.dto.SetScoreRequest;
import kr.hs.entrydsm.husky.domain.school.domain.School;
import kr.hs.entrydsm.husky.domain.user.dto.SelectTypeRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Entity(name = "ungraduated_application")
@NoArgsConstructor
public class UnGraduatedApplication extends GeneralApplication {

    @Id
    @Column(name = "user_receipt_code")
    private Integer receiptCode;

    public UnGraduatedApplication update(SelectTypeRequest dto) {
        if (dto.getGedPassDate() != null)
            super.updateGraduatedDate(dto.getGraduatedDate().atDay(1));
        return this;
    }

    public UnGraduatedApplication update(SetScoreRequest dto) {
        updateVolunteerAndAttendance(dto);
        updateGrade(dto);
        return this;
    }

    public UnGraduatedApplication(Integer receiptCode) {
        this.receiptCode = receiptCode;
    }

    @Builder(builderMethodName = "unGraduatedApplicationBuilder")
    public UnGraduatedApplication(Integer receiptCode, String studentNumber, School school, String schoolTel, LocalDate graduatedDate, Integer volunteerTime, Integer fullCutCount, Integer periodCutCount, Integer lateCount, Integer earlyLeaveCount, String korean, String social, String history, String math, String science, String techAndHome, String english) {
        super(studentNumber, school, schoolTel, graduatedDate, volunteerTime, fullCutCount, periodCutCount, lateCount, earlyLeaveCount, korean, social, history, math, science, techAndHome, english);
        this.receiptCode = receiptCode;
    }

}
