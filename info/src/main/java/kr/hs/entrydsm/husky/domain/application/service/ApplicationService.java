package kr.hs.entrydsm.husky.domain.application.service;

import kr.hs.entrydsm.husky.domain.application.dto.IntroResponse;
import kr.hs.entrydsm.husky.domain.application.dto.PlanResponse;
import kr.hs.entrydsm.husky.domain.application.exception.ApplicationNotFoundException;
import kr.hs.entrydsm.husky.domain.application.exception.ApplicationTypeUnmatchedException;
import kr.hs.entrydsm.husky.domain.user.dto.UserGradeResponse;
import kr.hs.entrydsm.husky.domain.user.exception.UserNotFoundException;
import kr.hs.entrydsm.husky.entities.applications.GEDApplication;
import kr.hs.entrydsm.husky.entities.applications.repositories.GEDApplicationRepository;
import kr.hs.entrydsm.husky.entities.users.User;
import kr.hs.entrydsm.husky.entities.users.enums.GradeType;
import kr.hs.entrydsm.husky.entities.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ApplicationService {

    private final UserRepository userRepository;
    private final GEDApplicationRepository gedApplicationRepository;

    public void addIntro(String intro) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        user.setSelfIntroduction(intro);
        userRepository.save(user);
    }

    public IntroResponse getIntro() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        return new IntroResponse(user.getSelfIntroduction());
    }

    public void addPlan(String plan) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        user.setStudyPlan(plan);
        userRepository.save(user);
    }

    public PlanResponse getPlan() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        return new PlanResponse(user.getStudyPlan());
    }

    public void addGedScore(int gedAverageScore) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        if (user.getGradeType() != GradeType.GED) throw new ApplicationTypeUnmatchedException();

        GEDApplication application = gedApplicationRepository.findByEmail(email)
                .orElseThrow(ApplicationNotFoundException::new);

        application.setGedAverageScore(gedAverageScore);
        gedApplicationRepository.save(application);
    }

}
