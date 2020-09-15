package kr.hs.entrydsm.husky.domain.user.service;

import kr.hs.entrydsm.husky.domain.application.domain.GEDApplication;
import kr.hs.entrydsm.husky.domain.application.domain.GeneralApplication;
import kr.hs.entrydsm.husky.domain.application.domain.GraduatedApplication;
import kr.hs.entrydsm.husky.domain.application.domain.UnGraduatedApplication;
import kr.hs.entrydsm.husky.domain.application.domain.adapter.GeneralApplicationAdapter;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GEDApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GraduatedApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.UnGraduatedApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.async.ApplicationAsyncRepository;
import kr.hs.entrydsm.husky.domain.application.exception.ApplicationNotFoundException;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.repositories.UserRepository;
import kr.hs.entrydsm.husky.domain.user.domain.repositories.async.UserAsyncRepository;
import kr.hs.entrydsm.husky.domain.user.dto.SelectTypeRequest;
import kr.hs.entrydsm.husky.domain.user.dto.UserTypeResponse;
import kr.hs.entrydsm.husky.domain.user.exception.UserNotFoundException;
import kr.hs.entrydsm.husky.global.config.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserTypeService {

    private final UserRepository userRepository;
    private final UserAsyncRepository userAsyncRepository;
    private final GEDApplicationRepository gedRepository;
    private final GraduatedApplicationRepository graduatedRepository;
    private final ApplicationAsyncRepository applicationAsyncRepository;
    private final UnGraduatedApplicationRepository unGraduatedRepository;

    private final AuthenticationFacade authenticationFacade;

    @Transactional
    public void updateUserType(SelectTypeRequest dto) {
        User user = userRepository.findById(authenticationFacade.getReceiptCode())
                .orElseThrow(UserNotFoundException::new);

        user.updateClassification(dto);
        userAsyncRepository.save(user);

        if (user.isGED()) {
            gedRepository.findById(user.getReceiptCode())
                    .or(() -> Optional.of(new GEDApplication(user.getReceiptCode())))
                    .map(ged -> ged.update(dto))
                    .ifPresent(applicationAsyncRepository::save);

        } else if (user.isGraduated()) {
            graduatedRepository.findById(user.getReceiptCode())
                    .or(() -> Optional.of(new GraduatedApplication(user.getReceiptCode())))
                    .map(graduated -> graduated.update(dto))
                    .ifPresent(applicationAsyncRepository::save);

        } else if (user.isUngraduated()) {
            unGraduatedRepository.findById(user.getReceiptCode())
                    .or(() -> Optional.of(new UnGraduatedApplication(user.getReceiptCode())))
                    .map(unGraduated -> unGraduated.update(dto))
                    .ifPresent(applicationAsyncRepository::save);
        }
    }

    public UserTypeResponse getUserType() {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        LocalDate graduatedDate = null;
        LocalDate gedPassDate = null;

        if (user.getGradeType() == null) return UserTypeResponse.response(user, null, null);

        switch (user.getGradeType()) {
            case GED:
                GEDApplication ged = gedRepository.findById(user.getReceiptCode())
                        .orElseThrow(ApplicationNotFoundException::new);
                gedPassDate = ged.getGedPassDate();
                break;

            case GRADUATED:
            case UNGRADUATED:
                GeneralApplication generalApplication = new GeneralApplicationAdapter(user).getGeneralApplication();
                graduatedDate = generalApplication.getGraduatedDate();
                break;
        }

        return UserTypeResponse.response(user, graduatedDate, gedPassDate);
    }

}
