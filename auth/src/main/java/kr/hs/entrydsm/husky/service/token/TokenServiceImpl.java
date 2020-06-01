package kr.hs.entrydsm.husky.service.token;

import io.jsonwebtoken.*;
import kr.hs.entrydsm.husky.exceptions.ExpiredTokenException;
import kr.hs.entrydsm.husky.exceptions.InvalidTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${auth.jwt.secret}")
    private String SECURITY_KEY;

    private String generateToken(String data, Long expire, String type) {
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
    public String generateAccessToken(String data) {
        return generateToken(data, 1000L * 3600 * 2, "access_token");
    }

    @Override
    public String generateRefreshToken(String data) {
        return generateToken(data, 1000L * 3600 * 24 * 30, "refresh_token");
    }

    @Override
    public String parseToken(String token) {
        String result;
        try {
            Claims body = Jwts.parser().setSigningKey(SECURITY_KEY.getBytes()).parseClaimsJws(token).getBody();
            result = body.get("email").toString();
            if (!body.get("type").equals("access_token")) throw new InvalidTokenException();
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        } catch (MalformedJwtException e) {
            throw new InvalidTokenException();
        }
        return result;
    }

    @Override
    public String parseRefreshToken(String token) {
        String result;
        try {
            Claims body = Jwts.parser().setSigningKey(SECURITY_KEY.getBytes()).parseClaimsJws(token).getBody();
            if(!body.get("type").equals("refresh_token")) throw new InvalidTokenException();
            result = body.get("email").toString();
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        } catch (MalformedJwtException e) {
            throw new InvalidTokenException();
        }
        return result;
    }

}
