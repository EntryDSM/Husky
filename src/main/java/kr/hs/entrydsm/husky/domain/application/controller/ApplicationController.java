package kr.hs.entrydsm.husky.domain.application.controller;

import kr.hs.entrydsm.husky.domain.application.dto.*;
import kr.hs.entrydsm.husky.domain.application.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/applications/me")
@RestController
public class ApplicationController {

    private final ApplicationService applicationService;

    @PatchMapping("/intro")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void setIntro(@RequestBody @Valid SetDocsRequest request) {
        applicationService.setIntro(request);
    }

    @GetMapping("/intro")
    public IntroResponse getIntro() {
        return applicationService.getIntro();
    }

    @PatchMapping("/plan")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void setPlan(@RequestBody @Valid SetDocsRequest request) {
        applicationService.setPlan(request);
    }

    @GetMapping("/plan")
    public PlanResponse getPlan() {
        return applicationService.getPlan();
    }

    @GetMapping("/score")
    public ScoreResponse getScore() {
        return applicationService.getScore();
    }

    @PatchMapping("/score")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void setScore(@RequestBody @Valid SetScoreRequest request) {
        applicationService.setScore(request);
    }

    @PatchMapping("/score/ged")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void setGedScore(@RequestBody @Valid SetGedScoreRequest request) {
        applicationService.setGedScore(request);
    }

}
