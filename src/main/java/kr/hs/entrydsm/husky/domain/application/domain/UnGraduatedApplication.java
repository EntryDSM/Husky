package kr.hs.entrydsm.husky.domain.application.domain;

import kr.hs.entrydsm.husky.domain.school.domain.School;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Getter
@Entity(name = "ungraduated_application")
@NoArgsConstructor
public class UnGraduatedApplication extends GeneralApplication {

    public UnGraduatedApplication(User user) {
        super(user);
    }

    @Builder(builderMethodName = "unGraduatedApplicationBuilder")
    public UnGraduatedApplication(User user, String studentNumber, School school, String schoolTel, Integer volunteerTime,
                              Integer fullCutCount, Integer periodCutCount, Integer lateCount, Integer earlyLeaveCount,
                              String korean, String social, String history, String math, String science,
                              String techAndHome, String english) {
        super(user, studentNumber, school, schoolTel, volunteerTime, fullCutCount, periodCutCount, lateCount,
                earlyLeaveCount, korean, social, history, math, science, techAndHome, english);
    }

}
