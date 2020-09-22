package kr.hs.entrydsm.husky.domain.application.domain.adapter;

import kr.hs.entrydsm.husky.domain.application.domain.GeneralApplication;
import kr.hs.entrydsm.husky.domain.application.domain.GraduatedApplication;
import kr.hs.entrydsm.husky.domain.application.domain.UnGraduatedApplication;
import kr.hs.entrydsm.husky.domain.application.dto.SetScoreRequest;
import kr.hs.entrydsm.husky.domain.school.domain.School;
import kr.hs.entrydsm.husky.domain.user.domain.enums.GradeType;
import kr.hs.entrydsm.husky.domain.user.dto.SetUserInfoRequest;
import lombok.Getter;

import static kr.hs.entrydsm.husky.domain.user.domain.enums.GradeType.GRADUATED;
import static kr.hs.entrydsm.husky.domain.user.domain.enums.GradeType.UNGRADUATED;

@Getter
public class GeneralApplicationAdapter {

    private final GradeType gradeType;
    private GraduatedApplication graduatedApplication;
    private UnGraduatedApplication unGraduatedApplication;

    public void update(School school) {
        if (isUngraduatedApplication())
            unGraduatedApplication.update(school);
        else if (isGraduatedApplication())
            graduatedApplication.update(school);
    }

    public void update(SetUserInfoRequest dto) {
        if (isUngraduatedApplication())
            unGraduatedApplication.update(dto);
        else if (isGraduatedApplication())
            graduatedApplication.update(dto);
    }

    public void update(SetScoreRequest dto) {
        if (isUngraduatedApplication())
            unGraduatedApplication.update(dto);
        else if (isGraduatedApplication())
            graduatedApplication.update(dto);
    }

    public GeneralApplicationAdapter(GraduatedApplication application) {
        this.gradeType = GRADUATED;
        this.graduatedApplication = application;
    }

    public GeneralApplicationAdapter(UnGraduatedApplication application) {
        this.gradeType = UNGRADUATED;
        this.unGraduatedApplication = application;
    }

    public String getStudentNumber() {
        return getGeneralApplication().getStudentNumber();
    }

    public String getSchoolCode() {
        School school = getSchool();
        return (school != null) ? school.getSchoolCode() : null;
    }

    public String getSchoolTel() {
        return getGeneralApplication().getSchoolTel();
    }

    public String getSchoolName() {
        School school = getSchool();
        return (school != null) ? school.getSchoolName() : null;
    }

    public School getSchool() {
        GeneralApplication application = getGeneralApplication();
        return (isSchoolEmpty(application)) ? null : application.getSchool();
    }

    private boolean isSchoolEmpty(GeneralApplication application) {
        return application.getSchool() == null;
    }

    private boolean isUngraduatedApplication() {
        return gradeType.equals(UNGRADUATED) && unGraduatedApplication != null;
    }

    private boolean isGraduatedApplication() {
        return gradeType.equals(GRADUATED) && graduatedApplication != null;
    }

    public GeneralApplication getGeneralApplication() {
        if (gradeType.equals(UNGRADUATED))
            return unGraduatedApplication;
        else
            return graduatedApplication;
    }

}
