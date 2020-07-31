package kr.hs.entrydsm.husky.domain.user.controller;

import kr.hs.entrydsm.husky.domain.user.dto.SelectTypeRequest;
import kr.hs.entrydsm.husky.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RequestMapping("/users/me")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PatchMapping("/type")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Transactional
    public void selectGradeType(@RequestBody SelectTypeRequest request) {
        userService.selectType(request);
    }
}
