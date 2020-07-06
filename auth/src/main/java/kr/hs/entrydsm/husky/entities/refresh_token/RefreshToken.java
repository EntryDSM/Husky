package kr.hs.entrydsm.husky.entities.refresh_token;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Builder
@RedisHash
public class RefreshToken {

    @Id
    private String userEmail;

    @Indexed
    private String refreshToken;

    @TimeToLive
    private final Long ttl;
    
}
