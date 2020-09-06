package kr.hs.entrydsm.husky.domain.schedule.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@RedisHash("schedule")
public class Schedule implements Serializable {

    @Id
    private String id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
