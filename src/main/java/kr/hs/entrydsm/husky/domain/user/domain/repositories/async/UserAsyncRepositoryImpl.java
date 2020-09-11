package kr.hs.entrydsm.husky.domain.user.domain.repositories.async;

import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserAsyncRepositoryImpl implements UserAsyncRepository {

    private final UserRepository userRepository;

    @Async
    @Override
    public void save(User user) {
        userRepository.save(user);
    }

}
