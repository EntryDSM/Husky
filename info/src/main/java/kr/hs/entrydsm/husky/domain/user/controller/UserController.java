package kr.hs.entrydsm.husky.domain.user.controller;

import kr.hs.entrydsm.husky.domain.user.dto.*;
import kr.hs.entrydsm.husky.domain.user.service.UserInfoService;
import kr.hs.entrydsm.husky.domain.user.service.UserStatusService;
import kr.hs.entrydsm.husky.domain.user.service.UserTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/users/me")
@RestController
public class UserController {

    private final UserTypeService userTypeService;
    private final UserInfoService userInfoService;
    private final UserStatusService userStatusService;

    @PatchMapping("/type")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void selectUserType(@RequestBody @Valid SelectTypeRequest request) {
        userTypeService.selectUserType(request);
    }

    @GetMapping("/type")
    public UserTypeResponse getUserType() {
        return userTypeService.getUserType();
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserInfoResponse serUserInfo(@RequestBody @Valid SetUserInfoRequest request) {
        return userInfoService.setUserInfo(request);
    }

    @PostMapping("/ged")
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserInfoResponse serGEDUserInfo(@RequestBody @Valid SetUserInfoRequest request) {
        return userInfoService.setUserInfo(request);
    }

    @GetMapping
    public UserInfoResponse getUserInfo() {
        return userInfoService.getUserInfo();
    }

    @GetMapping("/status")
    public UserStatusResponse getUserStatus() { return userStatusService.getStatus(); }

    @PatchMapping("/status")
    public UserStatusResponse finalSubmit() {
        return userStatusService.finalSubmit();
    }
}
