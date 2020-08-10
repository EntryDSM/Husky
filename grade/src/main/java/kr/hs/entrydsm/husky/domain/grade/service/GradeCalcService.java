package kr.hs.entrydsm.husky.domain.grade.service;

import org.springframework.stereotype.Service;

@Service
public interface GradeCalcService {

    void calcStudentGrade(int receiptCode);

}
