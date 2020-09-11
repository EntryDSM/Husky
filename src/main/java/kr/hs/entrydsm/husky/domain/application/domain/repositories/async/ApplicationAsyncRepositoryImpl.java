package kr.hs.entrydsm.husky.domain.application.domain.repositories.async;

import kr.hs.entrydsm.husky.domain.application.domain.GEDApplication;
import kr.hs.entrydsm.husky.domain.application.domain.GraduatedApplication;
import kr.hs.entrydsm.husky.domain.application.domain.UnGraduatedApplication;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GEDApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.GraduatedApplicationRepository;
import kr.hs.entrydsm.husky.domain.application.domain.repositories.UnGraduatedApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ApplicationAsyncRepositoryImpl implements ApplicationAsyncRepository {

    private final GEDApplicationRepository gedApplicationRepository;
    private final GraduatedApplicationRepository graduatedApplicationRepository;
    private final UnGraduatedApplicationRepository unGraduatedApplicationRepository;

    @Async
    @Override
    public void save(GEDApplication gedApplication) {
        gedApplicationRepository.save(gedApplication);
    }

    @Async
    @Override
    public void save(GraduatedApplication graduatedApplication) {
        graduatedApplicationRepository.save(graduatedApplication);
    }

    @Async
    @Override
    public void save(UnGraduatedApplication unGraduatedApplication) {
        unGraduatedApplicationRepository.save(unGraduatedApplication);
    }

}
