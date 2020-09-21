package kr.hs.entrydsm.husky.domain.application.domain.repositories;

import kr.hs.entrydsm.husky.domain.application.domain.GeneralApplication;
import kr.hs.entrydsm.husky.domain.user.domain.User;

public interface GeneralApplicationRepository {
    GeneralApplication findByUser(User user);
    boolean isUserApplicationEmpty(User user);
    boolean existsByUser(User user);
}
