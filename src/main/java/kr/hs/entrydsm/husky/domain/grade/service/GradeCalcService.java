package kr.hs.entrydsm.husky.domain.grade.service;

import kr.hs.entrydsm.husky.domain.application.domain.CalculatedScore;
import org.springframework.stereotype.Service;

@Service
public interface GradeCalcService {

    CalculatedScore calcStudentGrade(int receiptCode);

}
