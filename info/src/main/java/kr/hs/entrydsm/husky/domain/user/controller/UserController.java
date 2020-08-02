package kr.hs.entrydsm.husky.domain.user.controller;

import kr.hs.entrydsm.husky.domain.user.dto.SelectTypeRequest;
import kr.hs.entrydsm.husky.domain.user.dto.SetUserInfoRequest;
import kr.hs.entrydsm.husky.domain.user.dto.UserInfoResponse;
import kr.hs.entrydsm.husky.domain.user.dto.UserTypeResponse;
import kr.hs.entrydsm.husky.domain.user.service.UserInfoService;
import kr.hs.entrydsm.husky.domain.user.service.UserTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RequestMapping("/users/me")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserTypeService userTypeService;
    private final UserInfoService userInfoService;

    @PatchMapping("/type")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Transactional
    public void selectUserType(@RequestBody SelectTypeRequest request) {
        userTypeService.selectUserType(request);
    }

    @GetMapping("/type")
    public UserTypeResponse getUserType() {
        return userTypeService.getUserType();
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserInfoResponse serUserInfo(@RequestBody SetUserInfoRequest request) {
        return userInfoService.setUserInfo(request);
    }

    @PostMapping("/ged")
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserInfoResponse serGEDUserInfo(@RequestBody SetUserInfoRequest request) {
        return userInfoService.setUserInfo(request);
    }
}
