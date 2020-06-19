package kr.hs.entrydsm.husky.service.pdf;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.List;

public class PdfServiceImpl implements PdfService {

    @Value("${grade.pdf.tmp-path}")
    private String tmpPdfFilePath;

    private XWPFDocument docx;

    @Override
    public void load() {
        try {
            File originDocxFile = new ClassPathResource("static/originDocx.docx").getFile();
            docx = new XWPFDocument(OPCPackage.open(originDocxFile));
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public FileInputStream save() {
        PdfOptions options = PdfOptions.create();
        options.fontEncoding("UTF-8");
        FileInputStream fileInputStream = null;
        try {
            OutputStream out = new FileOutputStream(new File(tmpPdfFilePath));
            PdfConverter.getInstance().convert(docx, out, options);
            fileInputStream = new FileInputStream(tmpPdfFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileInputStream;
    }

    @Override
    public void replace(String key, String value) {
        for (XWPFParagraph p : docx.getParagraphs()) {
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    if (text != null && text.contains(key)) {
                        text = text.replace(key, value);
                        r.setText(text, 0);
                    }
                }
            }
        }
        for (XWPFTable tbl : docx.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        for (XWPFRun r : p.getRuns()) {
                            String text = r.getText(0);
                            if (text != null && text.contains(key)) {
                                text = text.replace(key, value);
                                r.setText(text,0);
                            }
                        }
                    }
                }
            }
        }
    }

}
