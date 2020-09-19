package kr.hs.entrydsm.husky.domain.grade.service;

import kr.hs.entrydsm.husky.domain.application.domain.CalculatedScore;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import org.springframework.stereotype.Service;

@Service
public interface GradeCalcService {

    CalculatedScore calcStudentGrade(User user);

}
