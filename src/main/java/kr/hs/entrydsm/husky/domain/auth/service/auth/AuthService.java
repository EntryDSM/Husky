package kr.hs.entrydsm.husky.domain.auth.service.auth;

import kr.hs.entrydsm.husky.domain.auth.dto.request.SignInRequest;
import kr.hs.entrydsm.husky.domain.auth.dto.response.TokenResponse;

public interface AuthService {
    TokenResponse signIn(SignInRequest dto);
    TokenResponse refreshToken(String refreshToken);
}
