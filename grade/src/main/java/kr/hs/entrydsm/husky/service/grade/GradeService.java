package kr.hs.entrydsm.husky.service.grade;

import java.io.File;

public interface GradeService {
    File getTmpPdf(String userEmail);
    File getFinalPdf(String userEmail);
}
