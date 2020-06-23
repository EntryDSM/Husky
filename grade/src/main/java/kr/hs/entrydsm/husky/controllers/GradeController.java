package kr.hs.entrydsm.husky.controllers;

import kr.hs.entrydsm.husky.service.grade.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/grade")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @GetMapping(value = "/application/temp", produces = MediaType.APPLICATION_PDF_VALUE)
    public InputStreamResource getTmpPdf() {

        return gradeService.getTmpPdf();
    }

    @GetMapping(value = "/application", produces = MediaType.APPLICATION_PDF_VALUE)
    public InputStreamResource getFinalPdf() {
        return gradeService.getFinalPdf();
    }
}
