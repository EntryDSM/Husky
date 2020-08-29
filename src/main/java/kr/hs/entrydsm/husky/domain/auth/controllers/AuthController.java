package kr.hs.entrydsm.husky.domain.auth.controllers;

import kr.hs.entrydsm.husky.domain.auth.dto.request.AccountRequest;
import kr.hs.entrydsm.husky.domain.auth.dto.response.TokenResponse;
import kr.hs.entrydsm.husky.domain.auth.service.auth.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping
    public TokenResponse signIn(@RequestBody @Valid AccountRequest accountRequest) {
        return authService.signIn(accountRequest);
    }

    @PutMapping
    public TokenResponse refreshToken(@RequestHeader("X-Refresh-Token") String refreshToken) {
        return authService.refreshToken(refreshToken);
    }

}
