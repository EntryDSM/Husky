package kr.hs.entrydsm.husky.domain.user.domain.repositories.async;

import kr.hs.entrydsm.husky.domain.user.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAsyncRepository {
    void save(User user);
}
