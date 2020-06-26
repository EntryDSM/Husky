package kr.hs.entrydsm.husky.entities.refresh_token;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import javax.persistence.Id;

@Getter
@Builder
@RedisHash(timeToLive = 3600 * 36)
public class RefreshToken {

    private static final Long MINUTE = 60L;

    @Id
    private String userEmail;

    private String refreshToken;

    @TimeToLive
    private Long ttl;
}
