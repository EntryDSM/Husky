package kr.hs.entrydsm.husky.domain.auth.domain.emaillimit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash(value = "email_limiter")
@AllArgsConstructor
public class EmailLimiter {

    @Id
    private final String email;

    @TimeToLive
    private Long count;

    public EmailLimiter update() {
        if (isBelowLimit()) {
            count += 6;
        }
        return this;
    }

    public boolean isBelowLimit() {
        return count <= 60;
    }

}
