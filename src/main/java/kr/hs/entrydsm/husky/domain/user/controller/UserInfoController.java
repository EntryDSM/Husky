package kr.hs.entrydsm.husky.domain.user.controller;

import kr.hs.entrydsm.husky.domain.image.service.ImageService;
import kr.hs.entrydsm.husky.domain.user.dto.*;
import kr.hs.entrydsm.husky.domain.user.service.info.UserInfoService;
import kr.hs.entrydsm.husky.domain.user.service.status.UserStatusService;
import kr.hs.entrydsm.husky.domain.user.service.type.UserTypeService;
import lombok.RequiredArgsConstructor;
import org.jets3t.service.CloudFrontServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;

@RequiredArgsConstructor
@RequestMapping("/users/me")
@RestController
public class UserInfoController {

    private final UserTypeService userTypeService;
    private final UserInfoService userInfoService;
    private final UserStatusService userStatusService;
    private final ImageService imageService;

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
    public UserInfoResponse setUserInfo(@RequestBody @Valid SetUserInfoRequest request)
            throws IOException, ParseException, CloudFrontServiceException {
        return userInfoService.setUserInfo(request);
    }

    @PatchMapping("/ged")
    @ResponseStatus(value = HttpStatus.OK)
    public UserInfoResponse setGEDUserInfo(@RequestBody @Valid SetUserInfoRequest request)
            throws IOException, ParseException, CloudFrontServiceException {
        return userInfoService.setUserInfo(request);
    }

    @GetMapping
    public UserInfoResponse getUserInfo() throws IOException, ParseException, CloudFrontServiceException {
        return userInfoService.getUserInfo();
    }

    @GetMapping("/status")
    public UserStatusResponse getUserStatus() { return userStatusService.getStatus(); }

    @PatchMapping("/status")
    @ResponseStatus(value = HttpStatus.OK)
    public UserStatusResponse finalSubmit() {
        return userStatusService.finalSubmit();
    }

    @GetMapping("/pass/first")
    public UserPassResponse isPassedFirst() { return userStatusService.isPassedFirst(); }

    @GetMapping("/pass/final")
    public UserPassResponse isPassedFinal() { return userStatusService.isPassedFinal(); }

    @PostMapping("/photo")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String uploadPhoto(@RequestPart MultipartFile file) throws Exception {
        return imageService.upload(file);
    }
}
