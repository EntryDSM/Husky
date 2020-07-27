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

    @Value("${auth.jwt.exp.refresh}")
    private Long refreshExp;

    @Value("${auth.jwt.prefix}")
    private String tokenType;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenResponse signIn(AccountRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                    .map(User::getEmail)
                    .map(email -> {
                        String refreshToken = tokenProvider.generateRefreshToken(email);
                        return new RefreshToken(email, refreshToken, refreshExp);
                    })
                    .map(refreshTokenRepository::save)
                    .map(refresh -> {
                        String accessToken = tokenProvider.generateAccessToken(refresh.getEmail());
                        return new TokenResponse(accessToken, refresh.getRefreshToken(), tokenType);
                    })
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public TokenResponse refreshToken(String receivedToken) {
        if (!tokenProvider.isRefreshToken(receivedToken))
            throw new InvalidTokenException();
        
        return refreshTokenRepository.findByRefreshToken(receivedToken)
                .map(refreshToken -> {
                    String generatedRefreshToken = tokenProvider.generateRefreshToken(refreshToken.getEmail());
                    return refreshToken.update(generatedRefreshToken, refreshExp);
                })
                .map(refreshTokenRepository::save)
                .map(refreshToken -> {
                    String generatedAccessToken = tokenProvider.generateAccessToken(refreshToken.getEmail());
                    return new TokenResponse(generatedAccessToken, refreshToken.getRefreshToken(), tokenType);
                })
                .orElseThrow(ExpiredTokenException::new);
    }

}
