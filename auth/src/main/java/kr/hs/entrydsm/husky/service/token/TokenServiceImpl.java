package kr.hs.entrydsm.husky.service.token;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private static String SECURITY_KEY = "";

    private static String generateToken(Object data, Long expire, String type) {
        long nowMillis = System.currentTimeMillis();

        JwtBuilder builder = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date(nowMillis))
                .setHeaderParam("typ", "JWT")
                .claim("email", data)
                .claim("type", type)
                .setExpiration(new Date(nowMillis + expire))
                .signWith(SignatureAlgorithm.HS256, SECURITY_KEY.getBytes());

        return builder.compact();
    }

    @Override
    public String generateAccessToken(Object data) {
        return generateToken(data, 1000L * 3600, "access_token");
    }

    @Override
    public String generateRefreshToken(Object data) {
        return generateToken(data, 1000L * 3600 * 24 * 30, "refresh_token");
    }

    public String parseToken(String token) {
        String email = Jwts.parser().setSigningKey(SECURITY_KEY.getBytes()).parseClaimsJws(token).getBody().get("email").toString();
        String result;
        try {
            result = email;
            if (!Jwts.parser().setSigningKey(SECURITY_KEY.getBytes()).parseClaimsJws(token).getBody().get("type").equals("access_token")) throw new InvalidTokenException();
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        } catch (MalformedJwtException e) {
            throw new InvalidTokenException();
        }
        return result;
    }

    public String parseRefreshToken(String token) {
        try {
            if(!Jwts.parser().setSigningKey(SECURITY_KEY.getBytes()).parseClaimsJws(token).getBody().get("type").equals("refresh_token")) throw new InvalidTokenException();
            token = Jwts.parser().setSigningKey(SECURITY_KEY.getBytes()).parseClaimsJws(token).getBody().get("user_id").toString();
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        } catch (MalformedJwtException e) {
            throw new InvalidTokenException();
        }
        return Integer.parseInt(token);
    }

}
