package kr.hs.entrydsm.husky.domain.auth.service.refreshtoken;

import kr.hs.entrydsm.husky.domain.auth.domain.refreshtoken.RefreshToken;
import kr.hs.entrydsm.husky.domain.auth.domain.refreshtoken.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Async
    @Override
    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

}
