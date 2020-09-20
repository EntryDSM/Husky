package kr.hs.entrydsm.husky.domain.application.service;

import kr.hs.entrydsm.husky.domain.application.domain.GEDApplication;
import kr.hs.entrydsm.husky.domain.application.domain.GraduatedApplication;
import kr.hs.entrydsm.husky.domain.application.domain.UnGraduatedApplication;
import kr.hs.entrydsm.husky.domain.application.domain.adapter.GeneralApplicationAdapter;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GEDApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GraduatedApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.UnGraduatedApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.async.GeneralApplicationAsyncRepositoryImpl;
import kr.hs.entrydsm.husky.domain.application.dto.*;
import kr.hs.entrydsm.husky.domain.application.exception.ApplicationNotFoundException;
import kr.hs.entrydsm.husky.domain.application.exception.ApplicationTypeUnmatchedException;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.enums.GradeType;
import kr.hs.entrydsm.husky.domain.user.domain.repositories.UserRepository;
import kr.hs.entrydsm.husky.domain.user.exception.GradeTypeRequiredException;
import kr.hs.entrydsm.husky.domain.user.exception.UserNotFoundException;
import kr.hs.entrydsm.husky.global.config.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final UserRepository userRepository;
    private final GEDApplicationRepository gedApplicationRepository;
    private final GraduatedApplicationRepository graduatedApplicationRepository;
    private final UnGraduatedApplicationRepository unGraduatedApplicationRepository;
    private final GeneralApplicationAsyncRepositoryImpl generalApplicationAsyncRepository;

    private final AuthenticationFacade authenticationFacade;

    @Override
    public void setIntro(SetDocsRequest request) {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        user.setSelfIntroduction(request.getContent());
        userRepository.save(user);
    }

    @Override
    public IntroResponse getIntro() {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        return new IntroResponse(user.getSelfIntroduction());
    }

    @Override
    public void setPlan(SetDocsRequest request) {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        user.setStudyPlan(request.getContent());
        userRepository.save(user);
    }

    @Override
    public PlanResponse getPlan() {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        return new PlanResponse(user.getStudyPlan());
    }

    @Override
    public void setGedScore(SetGedScoreRequest request) {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        if (user.isGradeTypeEmpty())
            throw new GradeTypeRequiredException();

        if (!user.isGED())
            throw new ApplicationTypeUnmatchedException();

        GEDApplication application = gedApplicationRepository.findById(user.getReceiptCode())
                .orElseThrow(ApplicationNotFoundException::new);

        application.updateGedAverageScore(request.getGedAverageScore());
        gedApplicationRepository.save(application);
    }

    @Override
    public void setScore(SetScoreRequest dto) {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        if (user.isGradeTypeEmpty())
            throw new GradeTypeRequiredException();

        if (user.isGED())
            throw new ApplicationTypeUnmatchedException();

        GeneralApplicationAdapter adapter = createGeneralApplicationAdapter(receiptCode, user.getGradeType());
        Optional.of(adapter)
                .ifPresent(application -> {
                    application.update(dto);
                    generalApplicationAsyncRepository.save(application);
                });
    }

    @Override
    public ScoreResponse getScore() {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        if (user.isGradeTypeEmpty())
            throw new GradeTypeRequiredException();

        if (user.isGED()) {
            return gedApplicationRepository.findById(user.getReceiptCode())
                    .or(() -> createGEDApplication(user.getReceiptCode()))
                    .map(ScoreResponse::new)
                    .orElseThrow(ApplicationNotFoundException::new);
        }

        GeneralApplicationAdapter adapter = createGeneralApplicationAdapter(receiptCode, user.getGradeType());
        return new ScoreResponse(adapter);
    }

    private Optional<GEDApplication> createGEDApplication(int receiptCode) {
        GEDApplication application = new GEDApplication(receiptCode);
        return Optional.of(gedApplicationRepository.save(application));
    }

    public GeneralApplicationAdapter createGeneralApplicationAdapter(int receiptCode, GradeType gradeType) {
        switch (gradeType) {
            case GRADUATED:
                GraduatedApplication graduated = graduatedApplicationRepository.findById(receiptCode)
                        .orElseGet(() -> initGraduatedApplication(receiptCode));
                return new GeneralApplicationAdapter(graduated);

            case UNGRADUATED:
                UnGraduatedApplication unGraduated = unGraduatedApplicationRepository.findById(receiptCode)
                        .orElseGet(() -> initUnGraduatedApplication(receiptCode));
                return new GeneralApplicationAdapter(unGraduated);

            default:
                return null;
        }
    }
    
    private GraduatedApplication initGraduatedApplication(int receiptCode) {
        GraduatedApplication application = new GraduatedApplication(receiptCode);
        return graduatedApplicationRepository.save(application);
    }

    private UnGraduatedApplication initUnGraduatedApplication(int receiptCode) {
        UnGraduatedApplication application = new UnGraduatedApplication(receiptCode);
        return unGraduatedApplicationRepository.save(application);
    }

}
