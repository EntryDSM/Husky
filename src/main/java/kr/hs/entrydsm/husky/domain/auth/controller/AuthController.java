package kr.hs.entrydsm.husky.domain.auth.controller;

import kr.hs.entrydsm.husky.domain.auth.dto.request.AccountRequest;
import kr.hs.entrydsm.husky.domain.auth.dto.request.EmailRequest;
import kr.hs.entrydsm.husky.domain.auth.dto.request.VerifyCodeRequest;
import kr.hs.entrydsm.husky.domain.auth.dto.request.ChangePasswordRequest;
import kr.hs.entrydsm.husky.domain.auth.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;

    @PostMapping
    public void signUp(@RequestBody @Valid AccountRequest accountRequest) {
        userService.signUp(accountRequest);
    }

    @PostMapping("/email/verify")
    public void sendEmail(@RequestBody @Valid EmailRequest emailRequest) {
        userService.sendSignUpEmail(emailRequest);
    }

    @PutMapping("/email/verify")
    public void verifyEmail(@RequestBody @Valid VerifyCodeRequest verifyCodeRequest) {
        userService.authEmail(verifyCodeRequest);
    }

    @PostMapping("/email/password/verify")
    public void sendPasswordChangeEmail(@RequestBody @Valid EmailRequest emailRequest) {
        userService.sendPasswordChangeEmail(emailRequest);
    }

    @PutMapping("/password")
    public void changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(changePasswordRequest);
    }

}
