package kr.hs.entrydsm.husky.service.redis;

public interface RedisService {
    Object getData(String key);
    void setData(String key, Object value, Long time);
    Long getExpireTime(String key);
}
