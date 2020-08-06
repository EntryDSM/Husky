package kr.hs.entrydsm.husky.domain.application.controller;

import kr.hs.entrydsm.husky.domain.application.dto.IntroResponse;
import kr.hs.entrydsm.husky.domain.application.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@RequiredArgsConstructor
@RequestMapping("/applications/me")
@RestController
public class ApplicationController {

    private final ApplicationService applicationService;

    @PatchMapping("/intro")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addIntro(@Valid @NotBlank @NotEmpty @RequestBody String introduction) {
        applicationService.addIntro(introduction);
    }

    @GetMapping("/intro")
    public IntroResponse getIntro() {
        return applicationService.getIntro();
    }

}
