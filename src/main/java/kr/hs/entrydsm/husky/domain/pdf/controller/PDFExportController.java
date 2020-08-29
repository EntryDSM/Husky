package kr.hs.entrydsm.husky.domain.pdf.controller;

import kr.hs.entrydsm.husky.domain.pdf.service.PDFExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/grade/application")
public class PDFExportController {

    private final PDFExportService pdfExportService;

    @GetMapping(value = "/preview", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getPDFApplicationPreview() {
        return pdfExportService.getPDFApplicationPreview();
    }

    @GetMapping(value = "/final", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getFinalPDFApplication() {
        return pdfExportService.getFinalPDFApplication();
    }

}
