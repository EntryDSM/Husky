package kr.hs.entrydsm.husky.service.auth;

import kr.hs.entrydsm.husky.domains.request.AccountRequest;
import kr.hs.entrydsm.husky.domains.response.TokenResponse;
import kr.hs.entrydsm.husky.entities.refresh_token.RefreshToken;
import kr.hs.entrydsm.husky.entities.refresh_token.RefreshTokenRepository;
import kr.hs.entrydsm.husky.entities.users.User;
import kr.hs.entrydsm.husky.entities.users.repositories.UserRepository;
import kr.hs.entrydsm.husky.exceptions.ExpiredTokenException;
import kr.hs.entrydsm.husky.exceptions.InvalidTokenException;
import kr.hs.entrydsm.husky.exceptions.UserNotFoundException;
import kr.hs.entrydsm.husky.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;


    @Value("${auth.jwt.exp.refresh}")
    private final Long expirationTime;


    @Override
    public TokenResponse signIn(AccountRequest accountRequest) {
        User user = userRepository.findById(accountRequest.getEmail())
                .filter(u -> passwordEncoder.matches(accountRequest.getPassword(), u.getPassword()))
                .orElseThrow(UserNotFoundException::new);

        TokenResponse response = this.responseToken(user.getEmail());
        refreshTokenRepository.deleteById(user.getEmail());
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .userEmail(user.getEmail())
                        .refreshToken(response.getRefreshToken())
                        .ttl(expirationTime)
                        .build()
        );
        return response;
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        String email = jwtTokenProvider.getUserEmail(refreshToken);
        if (!jwtTokenProvider.isRefreshToken(refreshToken))
            throw new InvalidTokenException();

        refreshTokenRepository.findById(email).orElseThrow(ExpiredTokenException::new);

        TokenResponse response = this.responseToken(email);
        refreshTokenRepository.deleteById(email);
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .userEmail(email)
                        .refreshToken(response.getRefreshToken())
                        .ttl(expirationTime)
                        .build()
        );
        return response;
    }

    private TokenResponse responseToken(String email) {
        return TokenResponse.builder()
                .accessToken(jwtTokenProvider.generateAccessToken(email))
                .refreshToken(jwtTokenProvider.generateRefreshToken(email))
                .tokenType("Bearer")
                .build();
    }
}
