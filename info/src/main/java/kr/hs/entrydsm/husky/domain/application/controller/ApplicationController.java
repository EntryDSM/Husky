package kr.hs.entrydsm.husky.domain.application.controller;

import kr.hs.entrydsm.husky.domain.application.dto.IntroResponse;
import kr.hs.entrydsm.husky.domain.application.dto.PlanResponse;
import kr.hs.entrydsm.husky.domain.application.service.ApplicationService;
import kr.hs.entrydsm.husky.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RequiredArgsConstructor
@RequestMapping("/applications/me")
@RestController
public class ApplicationController {

    private final ApplicationService applicationService;

    @PatchMapping("/intro")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addIntro(@RequestBody HashMap<String, String> request) {
        checkParameter(request, "introduce");

        applicationService.addIntro(request.get("introduce"));
    }

    @GetMapping("/intro")
    public IntroResponse getIntro() {
        return applicationService.getIntro();
    }

    @PatchMapping("/plan")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addPlan(@RequestBody HashMap<String, String> request) {
        checkParameter(request, "plan");

        applicationService.addPlan(request.get("plan"));
    }

    @GetMapping("/plan")
    public PlanResponse getPlan() {
        return applicationService.getPlan();
    }

    @PatchMapping("/score/ged")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addGedScore(@RequestBody HashMap<String, String> request) {
        checkParameter(request, "score");
        int averageScore;
        try {
            averageScore = Integer.parseInt(request.get("score"));
        } catch (Exception e) {
            throw new BadRequestException();
        }

        applicationService.addGedScore(averageScore);
    }

    private void checkParameter(HashMap<String, String> request, String key) {
        if(request.get(key) == null) throw new BadRequestException();
    }

}
