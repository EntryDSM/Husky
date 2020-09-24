package kr.hs.entrydsm.husky.domain.user.service.info;

import kr.hs.entrydsm.husky.domain.user.dto.SetUserInfoRequest;
import kr.hs.entrydsm.husky.domain.user.dto.UserInfoResponse;
import org.jets3t.service.CloudFrontServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;

public interface UserInfoService {

    UserInfoResponse setUserInfo(SetUserInfoRequest request)
            throws IOException, ParseException, CloudFrontServiceException;
    UserInfoResponse getUserInfo() throws IOException, ParseException, CloudFrontServiceException;

}
