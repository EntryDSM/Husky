package kr.hs.entrydsm.husky.service.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private RedisTemplate<String, Object> redisTemplate;

    private ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

    @Override
    public Object getData(String key) {
        return valueOperations.get(key);
    }

    @Override
    public void setData(String key, Object value, Long time) {
        valueOperations.set(key, value, time);
    }

    @Override
    public Long getExpireTime(String key) {
        return redisTemplate.getExpire(key);
    }
}
