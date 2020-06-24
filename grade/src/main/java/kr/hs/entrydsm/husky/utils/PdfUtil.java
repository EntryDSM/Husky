package kr.hs.entrydsm.husky.utils;

import kr.hs.entrydsm.husky.exceptions.PdfLoadException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.util.List;
import java.util.UUID;

@Service
public class PdfUtil {

    private String tmpPdfFilePath = UUID.randomUUID().toString();

    private XWPFDocument docx;

    @PostConstruct
    public void pdfOpen() {
        try {
            File originDocxFile = new ClassPathResource("static/application_template.docx").getFile();
            docx = new XWPFDocument(OPCPackage.open(originDocxFile));
        } catch (InvalidFormatException | IOException e) {
            throw new PdfLoadException();
        }
    }

    @PreDestroy
    public void pdfClose() {
        new File(tmpPdfFilePath).deleteOnExit();
    }

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
        docx.getTables()
                .forEach(XWPFTable::getRows);
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
