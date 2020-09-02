package kr.hs.entrydsm.husky.domain.application.domain;

import kr.hs.entrydsm.husky.domain.school.domain.School;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
public class GraduatedApplication extends GeneralApplication {

    @Column
    private LocalDate graduatedDate;

    @Builder(builderMethodName = "graduatedApplicationBuilder")
    public GraduatedApplication(User user, String studentNumber, School school, String schoolTel, Integer volunteerTime, Integer fullCutCount, Integer periodCutCount, Integer lateCount, Integer earlyLeaveCount, String korean, String social, String history, String math, String science, String techAndHome, String english, LocalDate graduatedDate) {
        super(user, studentNumber, school, schoolTel, volunteerTime, fullCutCount, periodCutCount, lateCount, earlyLeaveCount, korean, social, history, math, science, techAndHome, english);
        this.graduatedDate = graduatedDate;
    }

}
