package kr.hs.entrydsm.husky.domain.auth.controller;

import kr.hs.entrydsm.husky.domain.auth.dto.request.SignInRequest;
import kr.hs.entrydsm.husky.domain.auth.dto.response.TokenResponse;
import kr.hs.entrydsm.husky.domain.auth.service.auth.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class TokenController {

    private final AuthServiceImpl authService;

    @PostMapping
    public TokenResponse signIn(@RequestBody @Valid SignInRequest dto) {
        return authService.signIn(dto);
    }

    @PutMapping
    public TokenResponse refreshToken(@RequestHeader("X-Refresh-Token") String refreshToken) {
        return authService.refreshToken(refreshToken);
    }

}
