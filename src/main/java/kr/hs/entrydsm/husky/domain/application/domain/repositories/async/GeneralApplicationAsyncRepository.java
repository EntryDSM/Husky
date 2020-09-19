package kr.hs.entrydsm.husky.domain.application.domain.repositories.async;

import kr.hs.entrydsm.husky.domain.application.domain.adapter.GeneralApplicationAdapter;

public interface GeneralApplicationAsyncRepository {

    void save(GeneralApplicationAdapter application);

}
