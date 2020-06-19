package kr.hs.entrydsm.husky.controllers;

import kr.hs.entrydsm.husky.service.grade.GradeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/grade")
@RequiredArgsConstructor
public class GradeController {

    private final Authentication authentication;

    private final GradeServiceImpl gradeService;

    @GetMapping(value = "/pdf-tmp", produces = MediaType.APPLICATION_PDF_VALUE)
    public InputStreamResource getTmpPdf() {

        return gradeService.getTmpPdf(authentication.getName());
    }

    @GetMapping("/pdf-final")
    public InputStreamResource getFinalPdf() {
        return gradeService.getFinalPdf(authentication.getName());
    }
}
