package kr.hs.entrydsm.husky.entities.verification;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import javax.persistence.Id;

@Getter
@Builder
@RedisHash(timeToLive = 60 * 3)
public class EmailVerification {

    public static final Long MINUTE = 60L;

    @Id
    private String email;

    private String authCode;

    private EmailVerificationStatus status;

    @TimeToLive
    private Long ttl;

    public void verify() {
        this.status = EmailVerificationStatus.VERIFIED;
        this.ttl = 3 * MINUTE;
    }

}
