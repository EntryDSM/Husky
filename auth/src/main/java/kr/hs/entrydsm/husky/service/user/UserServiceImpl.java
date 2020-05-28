package kr.hs.entrydsm.husky.service.user;

import kr.hs.entrydsm.husky.domains.request.TokenRequest;
import kr.hs.entrydsm.husky.domains.request.AccountRequest;
import kr.hs.entrydsm.husky.domains.response.TokenResponse;
import kr.hs.entrydsm.husky.entities.users.repositories.UserRepository;
import kr.hs.entrydsm.husky.service.token.TokenServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private TokenServiceImpl tokenService;

    @Override
    public TokenResponse signIn(AccountRequest accountRequest) {

        return responseToken("");
    }

    @Override
    public TokenResponse refreshToken(TokenRequest tokenRequest) {
        return responseToken("");
    }

    private TokenResponse responseToken(Object data) {
        return TokenResponse.builder()
                .accessToken(tokenService.generateAccessToken(""))
                .refreshToken(tokenService.generateRefreshToken(""))
                .build();
    }
}
