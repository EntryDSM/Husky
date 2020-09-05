package kr.hs.entrydsm.husky.domain.user.controller;

import kr.hs.entrydsm.husky.domain.user.dto.*;
import kr.hs.entrydsm.husky.domain.user.service.UserInfoService;
import kr.hs.entrydsm.husky.domain.user.service.UserStatusService;
import kr.hs.entrydsm.husky.domain.user.service.UserTypeService;
import kr.hs.entrydsm.husky.infra.s3.S3Service;
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

    private final UserTypeService userTypeService;
    private final UserInfoService userInfoService;
    private final UserStatusService userStatusService;
    private final S3Service s3Service;

    @PatchMapping("/type")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void updateUserType(@RequestBody @Valid SelectTypeRequest request) {
        userTypeService.updateUserType(request);
    }

    @GetMapping("/type")
    public UserTypeResponse getUserType() {
        return userTypeService.getUserType();
    }

    @PatchMapping
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public UserInfoResponse serUserInfo(@RequestBody @Valid SetUserInfoRequest request) {
        return userInfoService.setUserInfo(request);
    }

    @PatchMapping("/ged")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public UserInfoResponse serGEDUserInfo(@RequestBody @Valid SetUserInfoRequest request) {
        return userInfoService.setUserInfo(request);
    }

    @GetMapping
    public UserInfoResponse getUserInfo() throws MalformedURLException {
        return userInfoService.getUserInfo(s3Service);
    }

    @GetMapping("/status")
    public UserStatusResponse getUserStatus() { return userStatusService.getStatus(); }

    @PatchMapping("/status")
    public UserStatusResponse finalSubmit() {
        return userStatusService.finalSubmit();
    }

    @PostMapping("/photo")
    public String uploadPhoto(@RequestPart MultipartFile file) throws Exception {
        return s3Service.upload(file);
    }
}
