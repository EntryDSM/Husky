package kr.hs.entrydsm.husky.service.pdf;

import java.io.FileInputStream;

public interface PdfService {
    void pdfOpen();
    void pdfClose();
    FileInputStream save();
    void replace(String key, String value);
}
