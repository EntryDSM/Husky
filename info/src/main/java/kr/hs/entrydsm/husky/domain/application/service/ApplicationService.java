package kr.hs.entrydsm.husky.domain.application.service;

import kr.hs.entrydsm.husky.domain.application.dto.*;
import kr.hs.entrydsm.husky.domain.application.exception.ApplicationNotFoundException;
import kr.hs.entrydsm.husky.domain.application.exception.ApplicationTypeUnmatchedException;
import kr.hs.entrydsm.husky.domain.user.exception.UserNotFoundException;
import kr.hs.entrydsm.husky.entities.applications.GEDApplication;
import kr.hs.entrydsm.husky.entities.applications.GeneralApplication;
import kr.hs.entrydsm.husky.entities.applications.GraduatedApplication;
import kr.hs.entrydsm.husky.entities.applications.UnGraduatedApplication;
import kr.hs.entrydsm.husky.entities.applications.repositories.GEDApplicationRepository;
import kr.hs.entrydsm.husky.entities.applications.repositories.GraduatedApplicationRepository;
import kr.hs.entrydsm.husky.entities.applications.repositories.UnGraduatedApplicationRepository;
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
    private final GraduatedApplicationRepository graduatedApplicationRepository;
    private final UnGraduatedApplicationRepository unGraduatedApplicationRepository;

    public void setIntro(SetDocsRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        user.setSelfIntroduction(request.getContent());
        userRepository.save(user);
    }

    public IntroResponse getIntro() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        return new IntroResponse(user.getSelfIntroduction());
    }

    public void setPlan(SetDocsRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        user.setStudyPlan(request.getContent());
        userRepository.save(user);
    }

    public PlanResponse getPlan() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        return new PlanResponse(user.getStudyPlan());
    }

    public void setGedScore(SetGedScoreRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        if (!user.isGed())
            throw new ApplicationTypeUnmatchedException();

        GEDApplication application = gedApplicationRepository.findById(user.getReceiptCode())
                .orElseThrow(ApplicationNotFoundException::new);

        application.setGedAverageScore(request.getGedAverageScore());
        gedApplicationRepository.save(application);
    }

    public void setScore(SetScoreRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        if (!user.isGraduated() && !user.isUngraduated())
            throw new ApplicationTypeUnmatchedException();

        switch (user.getGradeType()) {
            case GRADUATED: {
                GraduatedApplication graduatedApplication = graduatedApplicationRepository.findById(user.getReceiptCode())
                        .orElseThrow(ApplicationNotFoundException::new);

                graduatedApplication.setScore(request.getVolunteerTime(), request.getFullCutCount(),
                        request.getPeriodCutCount(), request.getLateCount(), request.getEarlyLeaveCount(),
                        request.getKorean(), request.getSocial(), request.getHistory(), request.getMath(),
                        request.getScience(), request.getTechAndHome(), request.getEnglish());
                graduatedApplicationRepository.save(graduatedApplication);
                break;
            }

            case UNGRADUATED: {
                UnGraduatedApplication unGraduatedApplication = unGraduatedApplicationRepository.findById(user.getReceiptCode())
                        .orElseThrow(ApplicationNotFoundException::new);

                unGraduatedApplication.setScore(request.getVolunteerTime(), request.getFullCutCount(),
                        request.getPeriodCutCount(), request.getLateCount(), request.getEarlyLeaveCount(),
                        request.getKorean(), request.getSocial(), request.getHistory(), request.getMath(),
                        request.getScience(), request.getTechAndHome(), request.getEnglish());
                unGraduatedApplicationRepository.save(unGraduatedApplication);
                break;
            }
        }
    }

    public ScoreResponse getScore() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        GeneralApplication generalApplication;

        GradeType gradeType = user.getGradeType();

        switch (gradeType) {
            case GED: {
                GEDApplication gedApplication = gedApplicationRepository.findById(user.getReceiptCode())
                        .orElseThrow(ApplicationNotFoundException::new);
                return ScoreResponse.gedResponse(gedApplication, gradeType);
            }

            case UNGRADUATED: {
                generalApplication = unGraduatedApplicationRepository.findById(user.getReceiptCode())
                        .orElseThrow(ApplicationNotFoundException::new);
                break;
            }

            case GRADUATED: {
                generalApplication = graduatedApplicationRepository.findById(user.getReceiptCode())
                        .orElseThrow(ApplicationNotFoundException::new);
                break;
            }

            default:
                throw new ApplicationNotFoundException();
        }
        return ScoreResponse.response(generalApplication, gradeType);
    }

}
