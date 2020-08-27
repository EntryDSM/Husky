package kr.hs.entrydsm.husky.domain.pdf.service;

public interface PDFExportService {
    byte[] getPDFApplicationPreview();
    byte[] getFinalPDFApplication();
}
