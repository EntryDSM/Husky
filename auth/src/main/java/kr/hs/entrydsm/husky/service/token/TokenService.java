package kr.hs.entrydsm.husky.service.token;

public interface TokenService {
    String generateAccessToken(Object data);
    String generateRefreshToken(Object data);
}
