package kr.hs.entrydsm.husky.controllers;

import kr.hs.entrydsm.husky.domains.request.AccountRequest;
import kr.hs.entrydsm.husky.domains.request.VerifyCodeRequest;
import kr.hs.entrydsm.husky.domains.request.ChangePasswordRequest;
import kr.hs.entrydsm.husky.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping
    public void signUp(@RequestBody @Valid AccountRequest accountRequest) {
        userService.signUp(accountRequest);
    }

    @PostMapping("/email/verify")
    public void sendEmail(@RequestParam("email") @Email String email) {
        userService.sendEmail(email);
    }

    @PutMapping("/email/verify")
    public void verifyEmail(@RequestBody @Valid VerifyCodeRequest verifyCodeRequest) {
        userService.authEmail(verifyCodeRequest);
    }

    @PutMapping("/password")
    public void changePassword(@RequestHeader("Authorization") String token,
                               @RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(token, changePasswordRequest);
    }

}
