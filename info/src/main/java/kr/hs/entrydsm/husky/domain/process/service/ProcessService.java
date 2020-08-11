package kr.hs.entrydsm.husky.domain.process.service;

import kr.hs.entrydsm.husky.domain.process.dto.ProcessResponse;
import kr.hs.entrydsm.husky.domain.user.exception.UserNotFoundException;
import kr.hs.entrydsm.husky.entities.applications.*;
import kr.hs.entrydsm.husky.entities.applications.repositories.GEDApplicationRepository;
import kr.hs.entrydsm.husky.entities.applications.repositories.GraduatedApplicationRepository;
import kr.hs.entrydsm.husky.entities.users.User;
import kr.hs.entrydsm.husky.entities.users.enums.GradeType;
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
            GEDApplication gedApplication = gedApplicationRepository.findByEmail(user.getEmail())
                    .orElseThrow();
            if (gedApplication == null) return false;

            if (gedApplication.getGedPassDate() == null) return false;
        }
        if (user.isGraduated()) {
            GraduatedApplication graduatedApplication = graduatedApplicationRepository.findByEmail(user.getEmail())
                    .orElseThrow();
            if (graduatedApplication == null) return false;

            if (graduatedApplication.getGraduatedDate() == null) return false;
        }

        return user.isSetType();
    }

    private boolean checkInfo(User user) {
        if (!checkType(user)) return false;

        if (user.isUngraduated() || user.isGraduated()) {
            GeneralApplication application = user.getGeneralApplication();
            if (application == null) return false;

            if (!application.isSetStudentInfo()) return false;
        }

        return user.isSetInfo();
    }

    private boolean checkScore(User user) {
        if (!checkType(user)) return false;

        if (user.getGradeType() == GradeType.GED) {
            GEDApplication gedApplication = gedApplicationRepository.findByEmail(user.getEmail())
                    .orElseThrow();
            if (gedApplication == null) return false;

            return gedApplication.getGedAverageScore() != null;
        } else {
            GeneralApplication application = user.getGeneralApplication();
            if (application == null) return false;

            return application.isSetScore();
        }
    }

    private boolean checkDocs(User user) {
        return user.getSelfIntroduction() != null && user.getStudyPlan() != null;
    }

}
