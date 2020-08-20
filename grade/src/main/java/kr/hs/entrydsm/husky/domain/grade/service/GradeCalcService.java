package kr.hs.entrydsm.husky.domain.grade.service;

import kr.hs.entrydsm.husky.entities.applications.CalculatedScore;
import org.springframework.stereotype.Service;

@Service
public interface GradeCalcService {

    CalculatedScore calcStudentGrade(int receiptCode);

}
