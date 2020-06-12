package kr.hs.entrydsm.husky.service.auth;

import kr.hs.entrydsm.husky.domains.request.AccountRequest;
import kr.hs.entrydsm.husky.domains.response.TokenResponse;
import kr.hs.entrydsm.husky.entities.users.User;
import kr.hs.entrydsm.husky.entities.users.repositories.UserRepository;
import kr.hs.entrydsm.husky.exceptions.InvalidTokenException;
import kr.hs.entrydsm.husky.exceptions.UserNotFoundException;
import kr.hs.entrydsm.husky.service.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;


    @Override
    public TokenResponse signIn(AccountRequest accountRequest) {
        User user = userRepository.findById(accountRequest.getEmail())
                .filter(u -> passwordEncoder.matches(accountRequest.getPassword(), u.getPassword()))
                .orElseThrow(UserNotFoundException::new);

        return responseToken(user.getEmail());
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.isRefreshToken(refreshToken)) throw new InvalidTokenException();
        String email = jwtTokenProvider.getUserEmail(refreshToken);

        return responseToken(email);
    }

    private TokenResponse responseToken(String email) {
        return TokenResponse.builder()
                .accessToken(jwtTokenProvider.generateAccessToken(email))
                .refreshToken(jwtTokenProvider.generateRefreshToken(email))
                .tokenType("Bearer")
                .build();
    }
}
