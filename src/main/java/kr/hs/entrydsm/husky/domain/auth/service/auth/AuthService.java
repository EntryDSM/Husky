package kr.hs.entrydsm.husky.domain.auth.service.auth;

import kr.hs.entrydsm.husky.domain.auth.dto.request.AccountRequest;
import kr.hs.entrydsm.husky.domain.auth.dto.response.TokenResponse;

public interface AuthService {
    TokenResponse signIn(AccountRequest accountRequest);
    TokenResponse refreshToken(String refreshToken);
}
