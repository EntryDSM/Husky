package kr.hs.entrydsm.husky.service.user;

import kr.hs.entrydsm.husky.domains.request.TokenRequest;
import kr.hs.entrydsm.husky.domains.request.AccountRequest;
import kr.hs.entrydsm.husky.domains.response.TokenResponse;

public interface UserService {
    TokenResponse signIn(AccountRequest accountRequest);
    TokenResponse refreshToken(TokenRequest tokenRequest);
}
