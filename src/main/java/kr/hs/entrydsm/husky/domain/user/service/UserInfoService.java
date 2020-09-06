package kr.hs.entrydsm.husky.domain.user.service;

import kr.hs.entrydsm.husky.domain.application.domain.GeneralApplication;
import kr.hs.entrydsm.husky.domain.application.domain.adapter.GeneralApplicationAdapter;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.generalapplication.GeneralApplicationAsyncRepository;
import kr.hs.entrydsm.husky.domain.image.service.ImageService;
import kr.hs.entrydsm.husky.domain.school.domain.School;
import kr.hs.entrydsm.husky.domain.school.domain.repositories.SchoolRepository;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.repositories.UserRepository;
import kr.hs.entrydsm.husky.domain.user.dto.SetUserInfoRequest;
import kr.hs.entrydsm.husky.domain.user.dto.UserInfoResponse;
import kr.hs.entrydsm.husky.domain.user.exception.SchoolNotFoundException;
import kr.hs.entrydsm.husky.domain.user.exception.UserNotFoundException;
import kr.hs.entrydsm.husky.global.config.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.MalformedURLException;

@RequiredArgsConstructor
@Service
public class UserInfoService {

    private final UserRepository userRepository;
    private final GeneralApplicationAsyncRepository generalApplicationAsyncRepository;
    private final SchoolRepository schoolRepository;

    private final ImageService imageService;
    private final AuthenticationFacade authenticationFacade;

    @Transactional
    public UserInfoResponse setUserInfo(SetUserInfoRequest request) {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        user.update(request);
        user = userRepository.save(user);

        if (isGradeTypeEmpty(user) || user.isGED()) {
            return UserInfoResponse.builder()
                    .user(user).build();
        }

        GeneralApplicationAdapter application = new GeneralApplicationAdapter(user);
        if (!isSchoolCodeEmpty(request)) {
            School school = schoolRepository.findById(request.getSchoolCode())
                    .orElseThrow(SchoolNotFoundException::new);
            application.update(school);
        }

        application.update(request);
        generalApplicationAsyncRepository.save(application);

        School school = schoolRepository.findById(application.getSchoolCode())
                .orElseThrow(SchoolNotFoundException::new);

        return UserInfoResponse.builder()
                .user(user)
                .studentNumber(application.getStudentNumber())
                .schoolCode(application.getSchoolCode())
                .schoolTel(application.getSchoolTel())
                .schoolName(school.getSchoolName())
                .build();
    }

    public UserInfoResponse getUserInfo() throws MalformedURLException {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        if (isGradeTypeEmpty(user) || user.isGED() || isGeneralApplicationEmpty(user)) {
            return UserInfoResponse.builder()
                    .user(user).build();
        }

        GeneralApplication application = user.getGeneralApplication();
        return UserInfoResponse.builder()
                .user(user)
                .studentNumber(application.getStudentNumber())
                .schoolCode(application.getSchoolCode())
                .schoolTel(application.getSchoolTel())
                .schoolName(application.getSchoolName())
                .photo(imageService.generateObjectUrl(user.getUserPhoto()))
                .build();
    }

    private boolean isSchoolCodeEmpty(SetUserInfoRequest request) {
        return request.getSchoolCode() == null;
    }

    private boolean isGradeTypeEmpty(User user) {
        return user.getGradeType() == null;
    }

    private boolean isGeneralApplicationEmpty(User user) {
        return user.getGeneralApplication() == null;
    }

}
