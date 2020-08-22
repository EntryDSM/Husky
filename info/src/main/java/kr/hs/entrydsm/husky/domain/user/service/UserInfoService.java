package kr.hs.entrydsm.husky.domain.user.service;

import kr.hs.entrydsm.husky.domain.application.exception.ApplicationNotFoundException;
import kr.hs.entrydsm.husky.domain.user.dto.SetUserInfoRequest;
import kr.hs.entrydsm.husky.domain.user.dto.UserInfoResponse;
import kr.hs.entrydsm.husky.domain.user.exception.SchoolNotFoundException;
import kr.hs.entrydsm.husky.domain.user.exception.UserNotFoundException;
import kr.hs.entrydsm.husky.entities.applications.GraduatedApplication;
import kr.hs.entrydsm.husky.entities.applications.UnGraduatedApplication;
import kr.hs.entrydsm.husky.entities.applications.repositories.GEDApplicationRepository;
import kr.hs.entrydsm.husky.entities.applications.repositories.GraduatedApplicationRepository;
import kr.hs.entrydsm.husky.entities.applications.repositories.UnGraduatedApplicationRepository;
import kr.hs.entrydsm.husky.entities.schools.School;
import kr.hs.entrydsm.husky.entities.schools.repositories.SchoolRepository;
import kr.hs.entrydsm.husky.entities.users.User;
import kr.hs.entrydsm.husky.entities.users.enums.Sex;
import kr.hs.entrydsm.husky.entities.users.repositories.UserRepository;
import kr.hs.entrydsm.husky.security.AuthenticationFacade;
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
        User user = userRepository.findByReceiptCode(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        user.setInfo(request.getName(), Sex.valueOf(request.getSex()), request.getBirthDate(),
                request.getApplicantTel(), request.getParentTel(), request.getParentName(), request.getAddress(),
                request.getDetailAddress(), request.getPostCode(), request.getPhoto());
        userRepository.save(user);

        switch (user.getGradeType()) {
            case UNGRADUATED: {
                UnGraduatedApplication unGraduated = unGraduatedRepository.findByReceiptCode(receiptCode)
                        .orElseThrow(ApplicationNotFoundException::new);
                School school = schoolRepository.findById(request.getSchoolCode())
                        .orElseThrow(SchoolNotFoundException::new);

                unGraduated.setStudentInfo(request.getStudentNumber(), school, request.getSchoolTel());
                unGraduatedRepository.save(unGraduated);

                return UserInfoResponse.response(user, unGraduated.getStudentNumber(),
                        unGraduated.getSchool().getSchoolCode(), unGraduated.getSchoolTel());
            }
            case GRADUATED: {
                GraduatedApplication graduated = graduatedRepository.findByReceiptCode(receiptCode)
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
        User user = userRepository.findByReceiptCode(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        if (user.getGradeType() == null) throw new ApplicationNotFoundException();
        switch (user.getGradeType()) {
            case GRADUATED: {
                GraduatedApplication graduated = graduatedRepository.findByReceiptCode(receiptCode)
                        .orElseThrow(ApplicationNotFoundException::new);
                if (graduated.getSchool() == null) throw new SchoolNotFoundException();

                return UserInfoResponse.response(user, graduated.getStudentNumber(),
                        graduated.getSchool().getSchoolCode(), graduated.getSchoolTel());
            }
            case UNGRADUATED: {
                UnGraduatedApplication unGraduated = unGraduatedRepository.findByReceiptCode(receiptCode)
                        .orElseThrow(ApplicationNotFoundException::new);
                if (unGraduated.getSchool() == null) throw new SchoolNotFoundException();

                return UserInfoResponse.response(user, unGraduated.getStudentNumber(),
                        unGraduated.getSchool().getSchoolCode(), unGraduated.getSchoolTel());
            }
        }
        return UserInfoResponse.response(user, null, null, null);
    }

}
