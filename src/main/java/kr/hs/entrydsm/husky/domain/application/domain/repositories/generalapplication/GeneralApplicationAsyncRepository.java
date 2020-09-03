package kr.hs.entrydsm.husky.domain.application.domain.repositories.generalapplication;

import kr.hs.entrydsm.husky.domain.application.domain.adapter.GeneralApplicationAdapter;

public interface GeneralApplicationAsyncRepository {

    void save(GeneralApplicationAdapter application);

}
