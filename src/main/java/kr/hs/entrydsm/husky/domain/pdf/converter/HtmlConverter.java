package kr.hs.entrydsm.husky.domain.pdf.converter;

import com.itextpdf.html2pdf.ConverterProperties;

import java.io.ByteArrayOutputStream;

import static kr.hs.entrydsm.husky.domain.pdf.config.PdfConfig.createConverterProperties;

public class HtmlConverter {

    public static ByteArrayOutputStream convertHtmlToPdf(String html) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ConverterProperties converterProperties = createConverterProperties();
        com.itextpdf.html2pdf.HtmlConverter.convertToPdf(html, outputStream, converterProperties);
        return outputStream;
    }

}
