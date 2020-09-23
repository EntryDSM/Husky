package kr.hs.entrydsm.husky.domain.user.service.info;

import kr.hs.entrydsm.husky.domain.user.dto.SetUserInfoRequest;
import kr.hs.entrydsm.husky.domain.user.dto.UserInfoResponse;

import java.net.MalformedURLException;

public interface UserInfoService {

    UserInfoResponse setUserInfo(SetUserInfoRequest request) throws MalformedURLException;
    UserInfoResponse getUserInfo() throws MalformedURLException;

}
