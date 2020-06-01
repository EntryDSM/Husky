package kr.hs.entrydsm.husky.controllers;

import kr.hs.entrydsm.husky.domains.request.TokenRequest;
import kr.hs.entrydsm.husky.domains.request.AccountRequest;
import kr.hs.entrydsm.husky.domains.response.TokenResponse;
import kr.hs.entrydsm.husky.service.auth.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private AuthServiceImpl authService;

    @PostMapping
    public TokenResponse signIn(@RequestBody AccountRequest accountRequest) {
        return authService.signIn(accountRequest);
    }

    @PutMapping
    public TokenResponse refreshToken(@RequestBody TokenRequest tokenRequest) {
        return authService.refreshToken(tokenRequest);
    }

}
