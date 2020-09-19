package kr.hs.entrydsm.husky.domain.user.service;

import kr.hs.entrydsm.husky.domain.application.domain.GeneralApplication;
import kr.hs.entrydsm.husky.domain.application.domain.GraduatedApplication;
import kr.hs.entrydsm.husky.domain.application.domain.UnGraduatedApplication;
import kr.hs.entrydsm.husky.domain.application.domain.adapter.GeneralApplicationAdapter;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GeneralApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GraduatedApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.UnGraduatedApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.async.GeneralApplicationAsyncRepository;
import kr.hs.entrydsm.husky.domain.image.service.ImageService;
import kr.hs.entrydsm.husky.domain.school.domain.School;
import kr.hs.entrydsm.husky.domain.school.domain.repositories.SchoolRepository;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.repositories.UserRepository;
import kr.hs.entrydsm.husky.domain.user.domain.repositories.async.UserAsyncRepository;
import kr.hs.entrydsm.husky.domain.user.dto.SetUserInfoRequest;
import kr.hs.entrydsm.husky.domain.user.dto.UserInfoResponse;
import kr.hs.entrydsm.husky.domain.user.exception.SchoolNotFoundException;
import kr.hs.entrydsm.husky.domain.user.exception.UserNotFoundException;
import kr.hs.entrydsm.husky.global.config.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.MalformedURLException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserInfoService {

    private final UserRepository userRepository;
    private final UserAsyncRepository userAsyncRepository;
    private final GeneralApplicationRepository generalApplicationRepository;
    private final GraduatedApplicationRepository graduatedApplicationRepository;
    private final UnGraduatedApplicationRepository unGraduatedApplicationRepository;
    private final GeneralApplicationAsyncRepository generalApplicationAsyncRepository;
    private final SchoolRepository schoolRepository;

    private final ImageService imageService;
    private final AuthenticationFacade authenticationFacade;

    @Transactional
    public UserInfoResponse setUserInfo(SetUserInfoRequest request) throws MalformedURLException {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        user.updateInfo(request);
        userAsyncRepository.save(user);

        if (user.isGradeTypeEmpty() || user.isGED()) {
            return UserInfoResponse.builder()
                    .user(user).photo(getImageUrl(user)).build();
        }

        GeneralApplicationAdapter application = createGeneralApplicationAdapter(user);
        if (!isSchoolCodeEmpty(request)) {
            School school = schoolRepository.findById(request.getSchoolCode())
                    .orElseThrow(SchoolNotFoundException::new);
            application.update(school);
        }

        application.update(request);
        generalApplicationAsyncRepository.save(application);

        return UserInfoResponse.builder()
                .user(user)
                .studentNumber(application.getStudentNumber())
                .schoolCode(application.getSchoolCode())
                .schoolTel(application.getSchoolTel())
                .schoolName(application.getSchoolName())
                .photo(getImageUrl(user))
                .build();
    }

    private String getImageUrl(User user) throws MalformedURLException {
        return (!user.isPhotoEmpty()) ? imageService.generateObjectUrl(user.getUserPhoto()) : null;
    }

    public UserInfoResponse getUserInfo() throws MalformedURLException {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        if (user.isGradeTypeEmpty() || user.isGED() || !generalApplicationRepository.existsByUser(user)) {
            return UserInfoResponse.builder()
                    .user(user).photo(getImageUrl(user)).build();
        }

        GeneralApplication application = generalApplicationRepository.findByUser(user);
        return UserInfoResponse.builder()
                .user(user)
                .studentNumber(application.getStudentNumber())
                .schoolCode(application.getSchoolCode())
                .schoolTel(application.getSchoolTel())
                .schoolName(application.getSchoolName())
                .photo(getImageUrl(user))
                .build();
    }

    private boolean isSchoolCodeEmpty(SetUserInfoRequest request) {
        return request.getSchoolCode() == null;
    }

    private GeneralApplicationAdapter createGeneralApplicationAdapter(User user) {
        Optional<GraduatedApplication> graduated = graduatedApplicationRepository.findById(user.getReceiptCode());
        Optional<UnGraduatedApplication> unGraduated = unGraduatedApplicationRepository.findById(user.getReceiptCode());
        return new GeneralApplicationAdapter(user, graduated, unGraduated);
    }

}
