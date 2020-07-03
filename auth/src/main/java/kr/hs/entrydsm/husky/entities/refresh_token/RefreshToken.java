package kr.hs.entrydsm.husky.entities.refresh_token;

import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import javax.persistence.Id;

@Getter
@Builder
@RedisHash
public class RefreshToken {

    @Id
    private String userEmail;

    private String refreshToken;

    @TimeToLive
    private final Long ttl;
    
}
