package kr.hs.entrydsm.husky.domain.application.domain.repositories.async;

import kr.hs.entrydsm.husky.domain.application.domain.GEDApplication;
import kr.hs.entrydsm.husky.domain.application.domain.GraduatedApplication;
import kr.hs.entrydsm.husky.domain.application.domain.UnGraduatedApplication;

public interface ApplicationAsyncRepository {
    void save(GEDApplication gedApplication);
    void save(GraduatedApplication graduatedApplication);
    void save(UnGraduatedApplication unGraduatedApplication);
}
