package kr.hs.entrydsm.husky.domain.application.domain;

import kr.hs.entrydsm.husky.domain.application.dto.SetScoreRequest;
import kr.hs.entrydsm.husky.domain.school.domain.School;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.dto.SelectTypeRequest;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.function.Consumer;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
public class GraduatedApplication extends GeneralApplication {

    @Id
    @Column(name = "user_receipt_code")
    private Integer receiptCode;

    @OneToOne
    @PrimaryKeyJoinColumn
    private User user;

    @Column
    private LocalDate graduatedDate;

    public GraduatedApplication update(SelectTypeRequest dto) {
        if (dto.getGraduatedDate() != null)
            this.graduatedDate = dto.getGraduatedDate().atDay(1);
        return this;
    }

    public GraduatedApplication update(SetScoreRequest dto) {
        updateVolunteerAndAttendance(dto);
        updateGrade(dto);
        return this;
    }

    public GraduatedApplication(Integer receiptCode) {
        this.receiptCode = receiptCode;
    }

    @Builder(builderMethodName = "graduatedApplicationBuilder")
    public GraduatedApplication(Integer receiptCode, String studentNumber, School school, String schoolTel, Integer volunteerTime, Integer fullCutCount, Integer periodCutCount, Integer lateCount, Integer earlyLeaveCount, String korean, String social, String history, String math, String science, String techAndHome, String english, LocalDate graduatedDate) {
        super(studentNumber, school, schoolTel, volunteerTime, fullCutCount, periodCutCount, lateCount, earlyLeaveCount, korean, social, history, math, science, techAndHome, english);
        this.receiptCode = receiptCode;
        this.graduatedDate = graduatedDate;
    }

}
