package kr.hs.entrydsm.husky.domain.auth.domain.emaillimit;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailLimiterRepository extends CrudRepository<EmailLimiter, String> {
}
