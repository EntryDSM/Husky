package kr.hs.entrydsm.husky.entities.redis;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRepository extends KeyValueRepository<AuthEmail, String> {
}
