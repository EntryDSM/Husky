package kr.hs.entrydsm.husky.service.grade;

import org.springframework.core.io.InputStreamResource;


public interface GradeService {
    InputStreamResource getTmpPdf();
    InputStreamResource getFinalPdf();
}
