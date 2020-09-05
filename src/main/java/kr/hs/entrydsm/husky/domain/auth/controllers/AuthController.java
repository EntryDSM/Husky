package kr.hs.entrydsm.husky.domain.auth.controllers;

import kr.hs.entrydsm.husky.domain.auth.dto.request.AccountRequest;
import kr.hs.entrydsm.husky.domain.auth.dto.request.VerifyCodeRequest;
import kr.hs.entrydsm.husky.domain.auth.dto.request.ChangePasswordRequest;
import kr.hs.entrydsm.husky.domain.auth.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

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
    public void sendEmail(@RequestParam("email") @Email String email) {
        userService.sendSignUpEmail(email);
    }

    @PutMapping("/email/verify")
    public void verifyEmail(@RequestBody @Valid VerifyCodeRequest verifyCodeRequest) {
        userService.authEmail(verifyCodeRequest);
    }

    @PostMapping("/email/password/verify")
    public void sendPasswordChangeEmail(@RequestParam("email") @Email String email) {
        userService.sendPasswordChangeEmail(email);
    }

    @PutMapping("/password")
    public void changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(changePasswordRequest);
    }

}
