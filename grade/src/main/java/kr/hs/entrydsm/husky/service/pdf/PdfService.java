package kr.hs.entrydsm.husky.service.pdf;

import java.io.File;

public interface PdfService {
    void load();
    File save();
    void replace(String key, String value);
}
