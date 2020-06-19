package kr.hs.entrydsm.husky.service.pdf;

import java.io.FileInputStream;

public interface PdfService {
    void load();
    FileInputStream save();
    void replace(String key, String value);
}
