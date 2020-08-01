package kr.hs.entrydsm.husky.domain.user.controller;

import kr.hs.entrydsm.husky.domain.user.dto.SelectTypeRequest;
import kr.hs.entrydsm.husky.domain.user.dto.UserTypeResponse;
import kr.hs.entrydsm.husky.domain.user.service.UserTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RequestMapping("/users/me")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserTypeService userService;

    @PatchMapping("/type")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Transactional
    public void selectUserType(@RequestBody SelectTypeRequest request) {
        userService.selectUserType(request);
    }

    @GetMapping("/type")
    public UserTypeResponse getUserType() {
        return userService.getUserType();
    }
}
