package kr.hs.entrydsm.husky.entities.applications;

import kr.hs.entrydsm.husky.entities.schools.School;
import kr.hs.entrydsm.husky.entities.users.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Getter
@Entity
@NoArgsConstructor
public class GraduatedApplication extends GeneralApplication {

    @Builder(builderMethodName = "graduatedApplicationBuilder")
    public GraduatedApplication(User user, String studentNumber, School school, String schoolTel, Integer volunteerTime,
                                  Integer fullCutCount, Integer periodCutCount, Integer lateCount, Integer earlyLeaveCount,
                                  String korean, String social, String history, String math, String science,
                                  String techAndHome, String english) {
        super(user, studentNumber, school, schoolTel, volunteerTime, fullCutCount, periodCutCount, lateCount,
                earlyLeaveCount, korean, social, history, math, science, techAndHome, english);
    }

}
