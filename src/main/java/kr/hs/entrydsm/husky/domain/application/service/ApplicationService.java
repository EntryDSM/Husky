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
import kr.hs.entrydsm.husky.domain.user.domain.repositories.UserRepository;
import kr.hs.entrydsm.husky.domain.user.exception.GradeTypeRequiredException;
import kr.hs.entrydsm.husky.domain.user.exception.UserNotFoundException;
import kr.hs.entrydsm.husky.global.config.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ApplicationService {

    private final UserRepository userRepository;
    private final GEDApplicationRepository gedApplicationRepository;
    private final GraduatedApplicationRepository graduatedApplicationRepository;
    private final UnGraduatedApplicationRepository unGraduatedApplicationRepository;
    private final GeneralApplicationAsyncRepositoryImpl generalApplicationAsyncRepository;

    private final AuthenticationFacade authenticationFacade;

    public void setIntro(SetDocsRequest request) {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        user.setSelfIntroduction(request.getContent());
        userRepository.save(user);
    }

    public IntroResponse getIntro() {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        return new IntroResponse(user.getSelfIntroduction());
    }

    public void setPlan(SetDocsRequest request) {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        user.setStudyPlan(request.getContent());
        userRepository.save(user);
    }

    public PlanResponse getPlan() {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        return new PlanResponse(user.getStudyPlan());
    }

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

    public void setScore(SetScoreRequest dto) {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        if (user.isGradeTypeEmpty())
            throw new GradeTypeRequiredException();

        if (user.isGED())
            throw new ApplicationTypeUnmatchedException();

        Optional.of(createGeneralApplicationAdapter(user))
                .ifPresent(application -> {
                    application.update(dto);
                    generalApplicationAsyncRepository.save(application);
                });
    }

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

        GeneralApplicationAdapter adapter = createGeneralApplicationAdapter(user);
        return new ScoreResponse(adapter);
    }

    private Optional<GEDApplication> createGEDApplication(int receiptCode) {
        GEDApplication application = new GEDApplication(receiptCode);
        return Optional.of(gedApplicationRepository.save(application));
    }

    private GeneralApplicationAdapter createGeneralApplicationAdapter(User user) {
        Optional<GraduatedApplication> graduated = graduatedApplicationRepository.findById(user.getReceiptCode());
        Optional<UnGraduatedApplication> unGraduated = unGraduatedApplicationRepository.findById(user.getReceiptCode());
        return new GeneralApplicationAdapter(user, graduated, unGraduated);
    }

}
