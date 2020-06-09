package kr.hs.entrydsm.husky.controllers;

import kr.hs.entrydsm.husky.domains.request.AccountRequest;
import kr.hs.entrydsm.husky.domains.request.AuthCodeRequest;
import kr.hs.entrydsm.husky.domains.request.PasswordRequest;
import kr.hs.entrydsm.husky.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl signUpService;

    @PostMapping
    public void signIn(@RequestBody @Valid AccountRequest accountRequest) {
        signUpService.signUp(accountRequest);
    }

    @PostMapping("/email/verify")
    public void sendEmail(@RequestParam("email") @Email String email) {
        signUpService.sendEmail(email);
    }

    @PutMapping("/email/verify")
    public void authEmail(@RequestBody @Valid AuthCodeRequest authCodeRequest) {
        signUpService.authEmail(authCodeRequest);
    }

    @PutMapping("/{userId}/password")
    public void changePassword(@RequestHeader("Authorization") String token,
                               @PathVariable String userId,
                               @RequestParam @Valid PasswordRequest passwordRequest) {
        signUpService.changePassword(token, userId, passwordRequest);
    }

}
