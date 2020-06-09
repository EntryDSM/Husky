package kr.hs.entrydsm.husky.entities.redis;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;


@Getter
@Setter
@Builder
@RedisHash("auth_email")
public class AuthEmail {

    @Id
    private String email;

    private String authCode;

    private String status;

}
