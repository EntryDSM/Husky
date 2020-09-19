package kr.hs.entrydsm.husky.domain.pdf.controller;

import kr.hs.entrydsm.husky.domain.pdf.service.PDFExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@RestController
@RequestMapping("/grade/application")
public class PDFExportController {

    private final static String FILE_NAME = "대덕소프트웨어마이스터고등학교_입학원서";
    private final PDFExportService pdfExportService;

    @GetMapping(value = "/preview", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getPDFApplicationPreview() {
        return pdfExportService.getPDFApplicationPreview();
    }

    @GetMapping(value = "/final", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getFinalPDFApplication(HttpServletResponse response) {
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s.pdf\"", encodeFileName(FILE_NAME)));
        return pdfExportService.getFinalPDFApplication();
    }

    private String encodeFileName(String fileName) {
        return new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
    }

}
