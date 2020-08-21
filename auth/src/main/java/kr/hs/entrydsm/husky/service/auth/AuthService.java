package kr.hs.entrydsm.husky.service.auth;

import kr.hs.entrydsm.husky.dto.request.AccountRequest;
import kr.hs.entrydsm.husky.dto.response.TokenResponse;

public interface AuthService {
    TokenResponse signIn(AccountRequest accountRequest);
    TokenResponse refreshToken(String refreshToken);
}
