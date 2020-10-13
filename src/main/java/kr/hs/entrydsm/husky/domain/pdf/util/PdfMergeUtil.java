package kr.hs.entrydsm.husky.domain.pdf.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;

import java.io.*;

public class PdfMergeUtil {

    public static ByteArrayOutputStream concatPdf(ByteArrayOutputStream first, ByteArrayOutputStream second) {
        InputStream firstInputStream = new ByteArrayInputStream(first.toByteArray());
        InputStream secondInputStream = new ByteArrayInputStream(second.toByteArray());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfDocument mergedDocument = new PdfDocument(new PdfWriter(outputStream));
        PdfMerger merger = new PdfMerger(mergedDocument);

        PdfDocument firstDocument = null;
        PdfDocument secondDocument = null;

        try {
            firstDocument = new PdfDocument(new PdfReader(firstInputStream));
            secondDocument = new PdfDocument(new PdfReader(secondInputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (firstDocument != null) {
            merger.merge(firstDocument, 1, firstDocument.getNumberOfPages());
            firstDocument.close();
        }

        if (secondDocument != null) {
            merger.merge(secondDocument, 1, secondDocument.getNumberOfPages());
            secondDocument.close();
        }

        mergedDocument.close();
        return outputStream;
    }

}
