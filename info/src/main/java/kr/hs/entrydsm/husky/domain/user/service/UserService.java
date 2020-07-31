package kr.hs.entrydsm.husky.domain.user.service;

import kr.hs.entrydsm.husky.domain.user.dto.SelectTypeRequest;
import kr.hs.entrydsm.husky.domain.user.exception.UserNotFoundException;
import kr.hs.entrydsm.husky.entities.applications.GEDApplication;
import kr.hs.entrydsm.husky.entities.applications.GraduatedApplication;
import kr.hs.entrydsm.husky.entities.applications.UnGraduatedApplication;
import kr.hs.entrydsm.husky.entities.applications.repositories.GEDApplicationRepository;
import kr.hs.entrydsm.husky.entities.applications.repositories.GraduatedApplicationRepository;
import kr.hs.entrydsm.husky.entities.applications.repositories.UnGraduatedApplicationRepository;
import kr.hs.entrydsm.husky.entities.users.User;
import kr.hs.entrydsm.husky.entities.users.enums.AdditionalType;
import kr.hs.entrydsm.husky.entities.users.enums.ApplyType;
import kr.hs.entrydsm.husky.entities.users.enums.GradeType;
import kr.hs.entrydsm.husky.entities.users.repositories.UserRepository;
import kr.hs.entrydsm.husky.domain.user.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {


    private final UserRepository userRepository;
    private final GEDApplicationRepository gedRepository;
    private final GraduatedApplicationRepository graduatedRepository;
    private final UnGraduatedApplicationRepository unGraduatedRepository;


    public void selectType(SelectTypeRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        GradeType gradeType = GradeType.valueOf(request.getGrade_type().toUpperCase());
        ApplyType applyType = ApplyType.valueOf(request.getApply_type().toUpperCase());
        AdditionalType additionalType =
                AdditionalType.valueOf(request.getAdditional_type().toUpperCase());

        user.setClassification(gradeType, applyType, additionalType, request.is_daejeon());

        switch (gradeType) {
            case GED: {
                if(request.getGed_pass_date() == null) throw new BadRequestException();

                System.out.println(email);
                GEDApplication gedApplication = gedRepository.save(GEDApplication.builder()
                        .email(email)
                        .user(user)
                        .gedPassDate(request.getGed_pass_date())
                        .build());


                user.setApplication(gradeType, gedApplication, null, null);
                break;
            }
            case GRADUATED: {
                if(request.getGraduated_date() == null) throw new BadRequestException();

                GraduatedApplication graduatedApplication = GraduatedApplication.builder()
                        .email(email)
                        .user(user)
                        .graduatedDate(request.getGraduated_date())
                        .build();
                graduatedRepository.save(graduatedApplication);

                user.setApplication(gradeType, null, graduatedApplication, null);
                break;
            }
            case UNGRADUATED: {
                UnGraduatedApplication unGraduatedApplication = new UnGraduatedApplication(email, user);
                unGraduatedRepository.save(unGraduatedApplication);

                user.setApplication(gradeType, null, null, unGraduatedApplication);
                break;
            }
        }

        userRepository.save(user);

    }

}
