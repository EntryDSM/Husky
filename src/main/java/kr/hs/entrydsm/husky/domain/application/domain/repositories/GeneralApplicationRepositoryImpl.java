package kr.hs.entrydsm.husky.domain.application.domain.repositories;

import kr.hs.entrydsm.husky.domain.application.domain.GeneralApplication;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.enums.GradeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GeneralApplicationRepositoryImpl implements GeneralApplicationRepository {

    private final GEDApplicationRepository gedRepository;
    private final GraduatedApplicationRepository graduatedRepository;
    private final UnGraduatedApplicationRepository unGraduatedRepository;

    @Override
    public GeneralApplication findByUser(User user) {
        GradeType gradeType = user.getGradeType();
        switch (gradeType) {
            case GRADUATED:
                return graduatedRepository.findById(user.getReceiptCode()).orElse(null);
            case UNGRADUATED:
                return unGraduatedRepository.findById(user.getReceiptCode()).orElse(null);
            default:
                return null;
        }
    }

    @Override
    public boolean isUserApplicationEmpty(User user) {
        GradeType gradeType = user.getGradeType();
        switch (gradeType) {
            case GED:
                return !gedRepository.existsByReceiptCode(user.getReceiptCode());
            case GRADUATED:
                return !graduatedRepository.existsByReceiptCode(user.getReceiptCode());
            case UNGRADUATED:
                return !unGraduatedRepository.existsByReceiptCode(user.getReceiptCode());
            default:
                return true;
        }
    }

    @Override
    public boolean existsByUser(User user) {
        return findByUser(user) != null;
    }

}
