package kr.hs.entrydsm.husky.domain.user.controller;

import kr.hs.entrydsm.husky.domain.image.service.S3ImageServiceImpl;
import kr.hs.entrydsm.husky.domain.user.dto.*;
import kr.hs.entrydsm.husky.domain.user.service.info.UserInfoServiceImpl;
import kr.hs.entrydsm.husky.domain.user.service.status.UserStatusServiceImpl;
import kr.hs.entrydsm.husky.domain.user.service.type.UserTypeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.MalformedURLException;

@RequiredArgsConstructor
@RequestMapping("/users/me")
@RestController
public class UserInfoController {

    private final UserTypeServiceImpl userTypeService;
    private final UserInfoServiceImpl userInfoService;
    private final UserStatusServiceImpl userStatusService;
    private final S3ImageServiceImpl imageService;

    @PatchMapping("/type")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateUserType(@RequestBody @Valid SelectTypeRequest request) {
        userTypeService.updateUserType(request);
    }

    @GetMapping("/type")
    public UserTypeResponse getUserType() {
        return userTypeService.getUserType();
    }

    @PatchMapping
    @ResponseStatus(value = HttpStatus.OK)
    public UserInfoResponse setUserInfo(@RequestBody @Valid SetUserInfoRequest request) throws MalformedURLException {
        return userInfoService.setUserInfo(request);
    }

    @PatchMapping("/ged")
    @ResponseStatus(value = HttpStatus.OK)
    public UserInfoResponse setGEDUserInfo(@RequestBody @Valid SetUserInfoRequest request) throws MalformedURLException {
        return userInfoService.setUserInfo(request);
    }

    @GetMapping
    public UserInfoResponse getUserInfo() throws MalformedURLException {
        return userInfoService.getUserInfo();
    }

    @GetMapping("/status")
    public UserStatusResponse getUserStatus() { return userStatusService.getStatus(); }

    @PatchMapping("/status")
    @ResponseStatus(value = HttpStatus.OK)
    public UserStatusResponse finalSubmit() {
        return userStatusService.finalSubmit();
    }

    @PostMapping("/photo")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String uploadPhoto(@RequestPart MultipartFile file) throws Exception {
        return imageService.upload(file);
    }
}
