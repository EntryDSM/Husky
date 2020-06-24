package kr.hs.entrydsm.husky.utils;

import kr.hs.entrydsm.husky.exceptions.PdfLoadException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.UUID;

public class PdfUtil {

    private String tmpPdfFilePath = UUID.randomUUID().toString();

    private XWPFDocument docx;

    public void pdfOpen() {
        try {
            File originDocxFile = new ClassPathResource("static/application_template.docx").getFile();
            docx = new XWPFDocument(OPCPackage.open(originDocxFile));
        } catch (InvalidFormatException | IOException e) {
            throw new PdfLoadException();
        }
    }

    public FileInputStream save() {
        PdfOptions options = PdfOptions.create();
        options.fontEncoding("UTF-8");
        File file = null;
        FileInputStream fileInputStream = null;
        try {
            file = new File(tmpPdfFilePath);
            OutputStream out = new FileOutputStream(file);
            PdfConverter.getInstance().convert(docx, out, options);
            fileInputStream = new FileInputStream(tmpPdfFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert file != null;
            file.deleteOnExit();
        }

        return fileInputStream;
    }

    public void replace(String key, String value) {
        docx.getParagraphs().parallelStream()
                .flatMap(paragraph -> paragraph.getRuns().parallelStream())
                .filter(run -> !run.getText(0).isBlank() && run.getText(0).contains(key))
                .forEach(run -> run.setText(run.getText(0).replace(key, value), 0));

        docx.getTables().parallelStream()
                .flatMap(Table -> Table.getRows().parallelStream())
                .flatMap(TableRow -> TableRow.getTableCells().parallelStream())
                .flatMap(TableCell -> TableCell.getParagraphs().parallelStream())
                .flatMap(Paragraph -> Paragraph.getRuns().parallelStream())
                .filter(Run -> !Run.getText(0).isBlank() && Run.getText(0).contains(key))
                .forEach(Run -> Run.setText(Run.getText(0).replace(key, value), 0));
    }

}
