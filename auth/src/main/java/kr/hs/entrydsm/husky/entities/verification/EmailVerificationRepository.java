package kr.hs.entrydsm.husky.entities.verification;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationRepository extends KeyValueRepository<EmailVerification, String> {
}
