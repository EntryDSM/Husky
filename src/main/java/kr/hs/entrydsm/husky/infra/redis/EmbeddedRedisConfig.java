package kr.hs.entrydsm.husky.infra.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
@Profile("local")
public class EmbeddedRedisConfig {

    private RedisServer redisServer;

    public EmbeddedRedisConfig(@Value("${spring.redis.port}") int redisPort) {
        this.redisServer = new RedisServer(redisPort);
    }

    @PostConstruct
    public void runRedis() {
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }

}
