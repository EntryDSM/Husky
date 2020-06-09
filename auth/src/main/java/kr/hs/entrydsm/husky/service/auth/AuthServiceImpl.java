package kr.hs.entrydsm.husky.service.auth;

import kr.hs.entrydsm.husky.domains.request.AccountRequest;
import kr.hs.entrydsm.husky.domains.response.TokenResponse;
import kr.hs.entrydsm.husky.entities.users.User;
import kr.hs.entrydsm.husky.entities.users.repositories.UserRepository;
import kr.hs.entrydsm.husky.exceptions.UserNotFoundException;
import kr.hs.entrydsm.husky.service.token.TokenServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final TokenServiceImpl tokenService;

    private final PasswordEncoder passwordEncoder;


    @Override
    public TokenResponse signIn(AccountRequest accountRequest) {
        User user = userRepository.findById(accountRequest.getEmail())
                .filter(u -> passwordEncoder.matches(accountRequest.getPassword(), u.getPassword()))
                .orElseThrow(UserNotFoundException::new);
        // Refresh Token을 레디스에 저장? 아직 잘 모르겠음

        return responseToken(user.getEmail());
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        String email = tokenService.parseRefreshToken(refreshToken);

        return responseToken(email);
    }

    private TokenResponse responseToken(String email) {
        return TokenResponse.builder()
                .accessToken(tokenService.generateAccessToken(email))
                .refreshToken(tokenService.generateRefreshToken(email))
                .build();
    }
}
