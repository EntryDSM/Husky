package kr.hs.entrydsm.husky.domain.auth.service.refreshtoken;

import kr.hs.entrydsm.husky.domain.auth.domain.refreshtoken.RefreshToken;

public interface RefreshTokenService {

    void save(RefreshToken refreshToken);

}
