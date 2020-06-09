package kr.hs.entrydsm.husky.service.token;

public interface TokenService {
    String generateAccessToken(String data);
    String generateRefreshToken(String data);
    String parseToken(String token);
    String parseRefreshToken(String token);
}
