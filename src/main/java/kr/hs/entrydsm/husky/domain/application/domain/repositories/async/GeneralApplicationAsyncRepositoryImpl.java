package kr.hs.entrydsm.husky.domain.application.domain.repositories.async;

import kr.hs.entrydsm.husky.domain.application.domain.adapter.GeneralApplicationAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static kr.hs.entrydsm.husky.domain.user.domain.enums.GradeType.GRADUATED;
import static kr.hs.entrydsm.husky.domain.user.domain.enums.GradeType.UNGRADUATED;

@Repository
@RequiredArgsConstructor
public class GeneralApplicationAsyncRepositoryImpl implements GeneralApplicationAsyncRepository {

    private final ApplicationAsyncRepository applicationAsyncRepository;

    @Override
    public void save(GeneralApplicationAdapter application) {
        if (application.getGradeType().equals(UNGRADUATED))
            applicationAsyncRepository.save(application.getUnGraduatedApplication());
        else if (application.getGradeType().equals(GRADUATED))
            applicationAsyncRepository.save(application.getGraduatedApplication());
    }

}
