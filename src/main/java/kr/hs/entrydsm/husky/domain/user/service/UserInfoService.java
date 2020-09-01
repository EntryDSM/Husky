package kr.hs.entrydsm.husky.domain.user.service;

import kr.hs.entrydsm.husky.domain.application.domain.GeneralApplication;
import kr.hs.entrydsm.husky.domain.application.exception.ApplicationNotFoundException;
import kr.hs.entrydsm.husky.domain.user.dto.SetUserInfoRequest;
import kr.hs.entrydsm.husky.domain.user.dto.UserInfoResponse;
import kr.hs.entrydsm.husky.domain.user.exception.SchoolNotFoundException;
import kr.hs.entrydsm.husky.domain.user.exception.UserNotFoundException;
import kr.hs.entrydsm.husky.domain.application.domain.GraduatedApplication;
import kr.hs.entrydsm.husky.domain.application.domain.UnGraduatedApplication;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GEDApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GraduatedApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.UnGraduatedApplicationRepository;
import kr.hs.entrydsm.husky.domain.school.domain.School;
import kr.hs.entrydsm.husky.domain.school.domain.repositories.SchoolRepository;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.enums.Sex;
import kr.hs.entrydsm.husky.domain.user.domain.repositories.UserRepository;
import kr.hs.entrydsm.husky.global.config.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class UserInfoService {

    private final UserRepository userRepository;
    private final GEDApplicationRepository gedRepository;
    private final GraduatedApplicationRepository graduatedRepository;
    private final UnGraduatedApplicationRepository unGraduatedRepository;
    private final SchoolRepository schoolRepository;

    private final AuthenticationFacade authenticationFacade;

    @Transactional
    public UserInfoResponse setUserInfo(SetUserInfoRequest request) {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        user.setInfo(request.getName(), Sex.valueOf(request.getSex()), request.getBirthDate(),
                request.getApplicantTel(), request.getParentTel(), request.getParentName(), request.getAddress(),
                request.getDetailAddress(), request.getPostCode(), request.getPhoto());
        userRepository.save(user);

        switch (user.getGradeType()) {
            case UNGRADUATED: {
                UnGraduatedApplication unGraduated = unGraduatedRepository.findById(user.getReceiptCode())
                        .orElseThrow(ApplicationNotFoundException::new);
                School school = schoolRepository.findById(request.getSchoolCode())
                        .orElseThrow(SchoolNotFoundException::new);

                unGraduated.setStudentInfo(request.getStudentNumber(), school, request.getSchoolTel());
                unGraduatedRepository.save(unGraduated);

                return UserInfoResponse.response(user, unGraduated.getStudentNumber(),
                        unGraduated.getSchool().getSchoolCode(), unGraduated.getSchoolTel());
            }
            case GRADUATED: {
                GraduatedApplication graduated = graduatedRepository.findById(user.getReceiptCode())
                        .orElseThrow(ApplicationNotFoundException::new);
                School school = schoolRepository.findById(request.getSchoolCode())
                        .orElseThrow(SchoolNotFoundException::new);

                graduated.setStudentInfo(request.getStudentNumber(), school, request.getSchoolTel());
                graduatedRepository.save(graduated);

                return UserInfoResponse.response(user, graduated.getStudentNumber(),
                        graduated.getSchool().getSchoolCode(), graduated.getSchoolTel());
            }
        }

        return UserInfoResponse.response(user, null, null, null);
    }

    public UserInfoResponse getUserInfo() {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        if (user.getGradeType() == null || user.isGED()) {
            return UserInfoResponse.response(user, null, null, null);
        }

        GeneralApplication application = user.getGeneralApplication();
        if(application == null) throw new ApplicationNotFoundException();

        return UserInfoResponse.response(user, application.getStudentNumber(),
                application.getSchool().getSchoolCode(), application.getSchoolTel());
    }

}
