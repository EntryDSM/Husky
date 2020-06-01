package kr.hs.entrydsm.husky.controllers;

import kr.hs.entrydsm.husky.domains.request.AuthCodeRequest;
import kr.hs.entrydsm.husky.domains.request.PasswordRequest;
import kr.hs.entrydsm.husky.domains.request.SignUpRequest;
import kr.hs.entrydsm.husky.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private UserServiceImpl signUpService;

    @PostMapping
    public void signIn(@RequestBody SignUpRequest signUpRequest) {
        signUpService.signUp(signUpRequest);
    }

    @PostMapping("/email")
    public void sendEmail(@RequestParam String email) {
        signUpService.sendEmail(email);
    }

    @PostMapping("/valid-email")
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
