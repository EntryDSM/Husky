package kr.hs.entrydsm.husky.domain.user.service;

import kr.hs.entrydsm.husky.domain.application.domain.GeneralApplication;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GeneralApplicationRepository;
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

@RequiredArgsConstructor
@Service
public class UserInfoService {

    private final UserRepository userRepository;
    private final GeneralApplicationRepository generalApplicationRepository;
    private final SchoolRepository schoolRepository;

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

        GeneralApplication application = user.getGeneralApplication();
        if (!isSchoolCodeEmpty(request)) {
            School school = schoolRepository.findById(request.getSchoolCode())
                    .orElseThrow(SchoolNotFoundException::new);
            application.update(school);
        }

        application.update(request);
        generalApplicationRepository.save(application);

        return UserInfoResponse.builder()
                .user(user)
                .studentNumber(application.getStudentNumber())
                .schoolCode(application.getSchool().getSchoolCode())
                .schoolTel(application.getSchoolTel())
                .build();
    }

    public boolean isSchoolCodeEmpty(SetUserInfoRequest request) {
        return request.getSchoolCode() == null;
    }

    public UserInfoResponse getUserInfo() {
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
                .schoolCode(application.getSchool().getSchoolCode())
                .schoolTel(application.getSchoolTel())
                .build();
    }

    private boolean isGradeTypeEmpty(User user) {
        return user.getGradeType() == null;
    }

    private boolean isGeneralApplicationEmpty(User user) {
        return user.getGeneralApplication() == null;
    }

}
