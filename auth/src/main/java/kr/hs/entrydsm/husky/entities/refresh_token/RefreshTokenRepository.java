package kr.hs.entrydsm.husky.entities.refresh_token;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends KeyValueRepository<RefreshToken, String> {
}
