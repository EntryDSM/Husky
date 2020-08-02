package kr.hs.entrydsm.husky.domain.user.service;

import kr.hs.entrydsm.husky.domain.application.exception.ApplicationNotFoundException;
import kr.hs.entrydsm.husky.domain.application.exception.NotCreateApplicationException;
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
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserInfoService {

    private final UserRepository userRepository;
    private final GEDApplicationRepository gedRepository;
    private final GraduatedApplicationRepository graduatedRepository;
    private final UnGraduatedApplicationRepository unGraduatedRepository;
    private final SchoolRepository schoolRepository;

    public UserInfoResponse setUserInfo(SetUserInfoRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        user.setInfo(request.getName(), Sex.valueOf(request.getSex()), request.getBirth_date(),
                request.getApplicant_tel(), request.getParent_tel(), request.getParent_name(), request.getAddress(),
                request.getDetail_address(), request.getPost_code(), request.getPhoto());
        userRepository.save(user);

        UserInfoResponse response = UserInfoResponse.builder()
                .name(user.getName())
                .sex(user.getSex())
                .birth_date(user.getBirthDate().toString())
                .parent_name(user.getParentName())
                .parent_tel(user.getParentTel())
                .applicant_tel(user.getApplicantTel())
                .address(user.getAddress())
                .detail_address(user.getDetailAddress())
                .post_code(user.getPostCode())
                .photo(user.getUserPhoto())
                .build();

        if(user.getGradeType() == null) throw new NotCreateApplicationException();
        switch (user.getGradeType()) {
            case UNGRADUATED: {
                UnGraduatedApplication unGraduated = unGraduatedRepository.findByEmail(email)
                        .orElseThrow(ApplicationNotFoundException::new);
                School school = schoolRepository.findById(request.getSchool_code())
                        .orElseThrow(SchoolNotFoundException::new);

                unGraduated.setStudentInfo(request.getStudent_number(), school, request.getSchool_tel());
                unGraduatedRepository.save(unGraduated);

                response.setSchoolInfo(unGraduated.getStudentNumber(), school.getSchoolCode(),
                        unGraduated.getSchoolTel());
                break;
            }
            case GRADUATED: {
                GraduatedApplication graduated = graduatedRepository.findByEmail(email)
                        .orElseThrow(ApplicationNotFoundException::new);
                School school = schoolRepository.findById(request.getSchool_code())
                        .orElseThrow(SchoolNotFoundException::new);

                graduated.setStudentInfo(request.getStudent_number(), school, request.getSchool_tel());
                graduatedRepository.save(graduated);

                response.setSchoolInfo(request.getStudent_number(), request.getSchool_code(), request.getSchool_tel());
                break;
            }
        }

        return response;
    }

}
