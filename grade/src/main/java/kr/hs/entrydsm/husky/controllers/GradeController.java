package kr.hs.entrydsm.husky.controllers;

import kr.hs.entrydsm.husky.service.grade.GradeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/grade")
@RequiredArgsConstructor
public class GradeController {

    private final Authentication authentication;

    private final GradeServiceImpl gradeService;

    @GetMapping(value = "/pdf-tmp", produces = MediaType.APPLICATION_PDF_VALUE)
    public File getTmpPdf() {
        return gradeService.getTmpPdf(authentication.getName());
    }

    @GetMapping("/pdf-final")
    public File getFinalPdf() {
        return gradeService.getFinalPdf(authentication.getName());
    }
}
