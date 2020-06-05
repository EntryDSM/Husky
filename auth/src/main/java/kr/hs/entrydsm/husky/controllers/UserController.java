package kr.hs.entrydsm.husky.controllers;

import kr.hs.entrydsm.husky.domains.request.AuthCodeRequest;
import kr.hs.entrydsm.husky.domains.request.PasswordRequest;
import kr.hs.entrydsm.husky.domains.request.SignUpRequest;
import kr.hs.entrydsm.husky.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl signUpService;

    @PostMapping
    public void signIn(@RequestBody @Valid SignUpRequest signUpRequest) {
        signUpService.signUp(signUpRequest);
    }

    @PostMapping("/email/verify")
    public void sendEmail(@RequestParam @Valid String email) {
        signUpService.sendEmail(email);
    }

    @PutMapping("/email/verify")
    public void authEmail(AuthCodeRequest authCodeRequest) {
        signUpService.authEmail(authCodeRequest);
    }

    @PutMapping("/{userId}/password")
    public void changePassword(@RequestHeader("Authorization") String token,
                               @PathVariable Integer userId,
                               @RequestParam PasswordRequest passwordRequest) {
        signUpService.changePassword(token, userId, passwordRequest);
    }

}
