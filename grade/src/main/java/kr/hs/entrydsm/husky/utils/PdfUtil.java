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
        docx.getParagraphs().parallelStream()
                .flatMap(paragraph -> paragraph.getRuns().parallelStream())
                .filter(run -> !run.getText(0).isBlank() && run.getText(0).contains(key))
                .forEach(run -> run.setText(run.getText(0).replace(key, value), 0));

        docx.getTables().parallelStream()
                .flatMap(xwpfTable -> xwpfTable.getRows().parallelStream())
                .flatMap(xwpfTableRow -> xwpfTableRow.getTableCells().parallelStream())
                .flatMap(xwpfTableCell -> xwpfTableCell.getParagraphs().parallelStream())
                .flatMap(xwpfParagraph -> xwpfParagraph.getRuns().parallelStream())
                .filter(xwpfRun -> !xwpfRun.getText(0).isBlank() && xwpfRun.getText(0).contains(key))
                .forEach(xwpfRun -> xwpfRun.setText(xwpfRun.getText(0).replace(key, value), 0));
    }

}
