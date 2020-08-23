package kr.hs.entrydsm.husky.domain.user.service;

import kr.hs.entrydsm.husky.domain.application.exception.ApplicationNotFoundException;
import kr.hs.entrydsm.husky.domain.user.dto.SelectTypeRequest;
import kr.hs.entrydsm.husky.domain.user.dto.UserTypeResponse;
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
import kr.hs.entrydsm.husky.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class UserTypeService {

    private final UserRepository userRepository;
    private final GEDApplicationRepository gedRepository;
    private final GraduatedApplicationRepository graduatedRepository;
    private final UnGraduatedApplicationRepository unGraduatedRepository;

    private final AuthenticationFacade authenticationFacade;

    @Transactional
    public void selectUserType(SelectTypeRequest request) {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        GradeType gradeType = GradeType.valueOf(request.getGradeType().toUpperCase());
        ApplyType applyType = ApplyType.valueOf(request.getApplyType().toUpperCase());
        AdditionalType additionalType =
                AdditionalType.valueOf(request.getAdditionalType().toUpperCase());

        user.setClassification(gradeType, applyType, additionalType, request.isDaejeon());

        switch (gradeType) {
            case GED: {
                if (request.getGedPassDate() == null) throw new BadRequestException();

                gedRepository.save(GEDApplication.gedApplicationBuilder()
                        .user(user)
                        .gedPassDate(request.getGedPassDate())
                        .build());
                break;
            }
            case GRADUATED: {
                if (request.getGraduatedDate() == null) throw new BadRequestException();

                graduatedRepository.save(GraduatedApplication.graduatedApplicationBuilder()
                        .user(user)
                        .graduatedDate(request.getGraduatedDate())
                        .build());
                break;
            }
            case UNGRADUATED: {
                unGraduatedRepository.save(new UnGraduatedApplication(user));
                break;
            }
        }

    }

    public UserTypeResponse getUserType() {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        LocalDate graduatedDate = null;
        LocalDate gedPassDate = null;

        if (user.getGradeType() == null) throw new ApplicationNotFoundException();

        switch (user.getGradeType()) {
            case GED:
                GEDApplication ged = gedRepository.findById(user.getReceiptCode())
                        .orElseThrow(ApplicationNotFoundException::new);
                gedPassDate = ged.getGedPassDate();
                break;

            case GRADUATED:
                GraduatedApplication graduatedApplication = graduatedRepository.findById(user.getReceiptCode())
                        .orElseThrow(ApplicationNotFoundException::new);
                graduatedDate = graduatedApplication.getGraduatedDate();
                break;
        }

        return UserTypeResponse.response(user, graduatedDate, gedPassDate);
    }

}
