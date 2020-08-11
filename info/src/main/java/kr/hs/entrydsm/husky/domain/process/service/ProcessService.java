package kr.hs.entrydsm.husky.domain.process.service;

import kr.hs.entrydsm.husky.domain.process.dto.ProcessResponse;
import kr.hs.entrydsm.husky.domain.user.exception.UserNotFoundException;
import kr.hs.entrydsm.husky.entities.applications.*;
import kr.hs.entrydsm.husky.entities.applications.repositories.GEDApplicationRepository;
import kr.hs.entrydsm.husky.entities.applications.repositories.GraduatedApplicationRepository;
import kr.hs.entrydsm.husky.entities.users.User;
import kr.hs.entrydsm.husky.entities.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessService {

    private final UserRepository userRepository;
    private final GEDApplicationRepository gedApplicationRepository;
    private final GraduatedApplicationRepository graduatedApplicationRepository;

    public ProcessResponse getProcess() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        return ProcessResponse.builder()
                .type(checkType(user))
                .info(checkInfo(user))
                .score(checkScore(user))
                .document(checkDocs(user))
                .build();
    }

    private boolean checkType(User user) {
        if (user.isGed()) {
            return gedApplicationRepository.findByEmail(user.getEmail())
                    .map(application -> (application.getGedPassDate() != null) && user.isFilledType())
                    .orElse(false);
        }

        if (user.isGraduated()) {
            return graduatedApplicationRepository.findByEmail(user.getEmail())
                    .map(application -> (application.getGraduatedDate() != null) && user.isFilledType())
                    .orElse(false);
        }

        return user.isFilledType();
    }

    private boolean checkInfo(User user) {
        if (!checkType(user)) return false;

        if (user.isUngraduated() || user.isGraduated()) {
            GeneralApplication application = user.getGeneralApplication();

            return application != null && application.isFilledStudentInfo() && user.isFilledInfo();
        }

        return user.isFilledInfo();
    }

    private boolean checkScore(User user) {
        if (!checkType(user)) return false;

        if (user.isGed()) {
            return gedApplicationRepository.findByEmail(user.getEmail())
                    .map(application -> application.getGedAverageScore() != null)
                    .orElse(false);
        } else {
            GeneralApplication application = user.getGeneralApplication();

            return application != null && application.isFilledScore();
        }
    }

    private boolean checkDocs(User user) {
        return user.getSelfIntroduction() != null && user.getStudyPlan() != null;
    }

}
