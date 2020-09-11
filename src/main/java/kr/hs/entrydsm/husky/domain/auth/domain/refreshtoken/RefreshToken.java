package kr.hs.entrydsm.husky.domain.auth.domain.refreshtoken;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "refresh_token")
@AllArgsConstructor
public class RefreshToken {

    @Id
    private Integer receiptCode;

    @Indexed
    private String refreshToken;

    @TimeToLive
    private Long ttl;

    public RefreshToken update(String refreshToken, Long ttl) {
        this.refreshToken = refreshToken;
        this.ttl = ttl;
        return this;
    }
    
}
