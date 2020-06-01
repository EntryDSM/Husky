package kr.hs.entrydsm.husky.service.auth;

import kr.hs.entrydsm.husky.domains.request.TokenRequest;
import kr.hs.entrydsm.husky.domains.request.AccountRequest;
import kr.hs.entrydsm.husky.domains.response.TokenResponse;

public interface AuthService {
    TokenResponse signIn(AccountRequest accountRequest);
    TokenResponse refreshToken(TokenRequest tokenRequest);
}
