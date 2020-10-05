package kr.hs.entrydsm.husky.domain.process.service;

import kr.hs.entrydsm.husky.domain.application.domain.CalculatedScore;
import kr.hs.entrydsm.husky.domain.application.domain.GeneralApplication;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.CalculatedScoreRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GeneralApplicationRepository;
import kr.hs.entrydsm.husky.domain.process.dto.ProcessResponse;
import kr.hs.entrydsm.husky.domain.user.exception.UserNotFoundException;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GEDApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GraduatedApplicationRepository;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.repositories.UserRepository;
import kr.hs.entrydsm.husky.global.config.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static kr.hs.entrydsm.husky.global.util.Validator.*;

@RequiredArgsConstructor
@Service
public class ProcessServiceImpl implements ProcessService {

    private final UserRepository userRepository;
    private final GEDApplicationRepository gedApplicationRepository;
    private final GeneralApplicationRepository generalApplicationRepository;
    private final GraduatedApplicationRepository graduatedApplicationRepository;

    private final AuthenticationFacade authenticationFacade;

    @Override
    public ProcessResponse getProcess() {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        return ProcessResponse.builder()
                .type(checkType(user))
                .info(checkInfo(user))
                .score(checkScore(user))
                .document(checkDocs(user))
                .build();
    }

    @Override
    public boolean allCheck(User user, CalculatedScore score) {
        return checkType(user) && checkDocs(user) && checkInfo(user) && checkScore(user) &&
                checkConversionScore(user, score);
    }

    private boolean checkType(User user) {
        if (user.isGED()) {
            return gedApplicationRepository.findById(user.getReceiptCode())
                    .map(application -> (application.getGedPassDate() != null) && isFilledType(user))
                    .orElse(false);
        }

        if (user.isGraduated()) {
            return graduatedApplicationRepository.findById(user.getReceiptCode())
                    .map(application -> (application.getGraduatedDate() != null) && isFilledType(user))
                    .orElse(false);
        }

        return this.isFilledType(user);
    }

    private boolean isFilledType(User user) {
        return !user.isGradeTypeEmpty() && user.getApplyType() != null
                && user.getAdditionalType() != null && !generalApplicationRepository.isUserApplicationEmpty(user);
    }

    private boolean checkInfo(User user) {
        if (!checkType(user)) return false;

        if (user.isUngraduated() || user.isGraduated()) {
            GeneralApplication application = generalApplicationRepository.findByUser(user);
            return application != null && application.isFilledStudentInfo() && user.isFilledInfo();
        }

        return user.isFilledInfo();
    }

    private boolean checkScore(User user) {
        if (!checkType(user)) return false;

        if (user.isGED()) {
            return gedApplicationRepository.findById(user.getReceiptCode())
                    .map(application -> application.getGedAverageScore() != null)
                    .orElse(false);
        } else {
            GeneralApplication application = generalApplicationRepository.findByUser(user);
            return application != null && application.isFilledScore();
        }
    }

    private boolean checkDocs(User user) {
        return isExists(user.getSelfIntroduction()) && isExists(user.getStudyPlan());
    }

    private boolean checkConversionScore(User user, CalculatedScore score) {
        if (user.isGED()) {
            return isEqualTo(score.getAttendanceScore(), 15) &&
                    isGreaterThanOrEqualTo(score.getVolunteerScore(), BigDecimal.valueOf(3)) &&
                    isZero(score.getFirstGradeScore()) &&
                    isZero(score.getSecondGradeScore()) &&
                    isZero(score.getThirdGradeScore()) &&
                    isPositive(score.getConversionScore());
        }

        return isGreaterThanOrEqualTo(score.getAttendanceScore(), 0) &&
                isGreaterThanOrEqualTo(score.getVolunteerScore(), BigDecimal.valueOf(3)) &&
                isPositive(score.getFirstGradeScore()) &&
                isPositive(score.getSecondGradeScore()) &&
                isPositive(score.getThirdGradeScore()) &&
                isPositive(score.getConversionScore());
    }

}
