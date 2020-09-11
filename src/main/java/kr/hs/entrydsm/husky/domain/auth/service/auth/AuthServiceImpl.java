package kr.hs.entrydsm.husky.domain.auth.service.auth;

import kr.hs.entrydsm.husky.domain.auth.domain.refreshtoken.RefreshToken;
import kr.hs.entrydsm.husky.domain.auth.domain.refreshtoken.RefreshTokenRepository;
import kr.hs.entrydsm.husky.domain.auth.dto.request.SignInRequest;
import kr.hs.entrydsm.husky.domain.auth.dto.response.TokenResponse;
import kr.hs.entrydsm.husky.domain.auth.exceptions.ExpiredTokenException;
import kr.hs.entrydsm.husky.domain.auth.exceptions.InvalidTokenException;
import kr.hs.entrydsm.husky.domain.auth.service.refreshtoken.RefreshTokenService;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.repositories.UserRepository;
import kr.hs.entrydsm.husky.global.config.security.JwtTokenProvider;
import kr.hs.entrydsm.husky.global.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
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
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public TokenResponse signIn(SignInRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                    .map(User::getReceiptCode)
                    .map(receiptCode -> {
                        try {
                            authenticationManager.authenticate(request.getAuthToken(receiptCode));
                        } catch (AuthenticationException e) {
                            throw new BadCredentialsException(e.getLocalizedMessage());
                        }
                        String accessToken = tokenProvider.generateAccessToken(receiptCode);
                        String refreshToken = tokenProvider.generateRefreshToken(receiptCode);
                        refreshTokenService.save(new RefreshToken(receiptCode, refreshToken, refreshExp));
                        return new TokenResponse(accessToken, refreshToken, tokenType);
                    })
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public TokenResponse refreshToken(String receivedToken) {
        if (!tokenProvider.isRefreshToken(receivedToken))
            throw new InvalidTokenException();
        
        return refreshTokenRepository.findByRefreshToken(receivedToken)
                .map(refreshToken -> {
                    String generatedRefreshToken = tokenProvider.generateRefreshToken(refreshToken.getReceiptCode());
                    return refreshToken.update(generatedRefreshToken, refreshExp);
                })
                .map(refreshTokenRepository::save)
                .map(refreshToken -> {
                    String generatedAccessToken = tokenProvider.generateAccessToken(refreshToken.getReceiptCode());
                    return new TokenResponse(generatedAccessToken, refreshToken.getRefreshToken(), tokenType);
                })
                .orElseThrow(ExpiredTokenException::new);
    }

}
