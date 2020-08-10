package kr.hs.entrydsm.husky.domain.application.controller;

import kr.hs.entrydsm.husky.domain.application.dto.IntroResponse;
import kr.hs.entrydsm.husky.domain.application.dto.PlanResponse;
import kr.hs.entrydsm.husky.domain.application.dto.ScoreResponse;
import kr.hs.entrydsm.husky.domain.application.service.ApplicationService;
import kr.hs.entrydsm.husky.domain.application.dto.AddScoreRequest;
import kr.hs.entrydsm.husky.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RequiredArgsConstructor
@RequestMapping("/applications/me")
@RestController
public class ApplicationController {

    private final ApplicationService applicationService;

    @PatchMapping("/intro")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addIntro(@RequestBody HashMap<String, String> request) {
        if (request.get("introduce") == null) throw new BadRequestException();

        applicationService.addIntro(request.get("introduce"));
    }

    @GetMapping("/intro")
    public IntroResponse getIntro() {
        return applicationService.getIntro();
    }

    @PatchMapping("/plan")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addPlan(@RequestBody HashMap<String, String> request) {
        if (request.get("plan") == null) throw new BadRequestException();

        applicationService.addPlan(request.get("plan"));
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
    public void addScore(@RequestBody @Valid AddScoreRequest request) {
        applicationService.addScore(request);
    }

    @PatchMapping("/score/ged")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addGedScore(@RequestBody HashMap<String, Integer> request) {
        if (request.get("score") == null) throw new BadRequestException();
        int averageScore = request.get("score");

        applicationService.addGedScore(averageScore);
    }

}
