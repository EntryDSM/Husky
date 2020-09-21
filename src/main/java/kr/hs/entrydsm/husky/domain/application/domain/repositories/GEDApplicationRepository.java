package kr.hs.entrydsm.husky.domain.application.domain.repositories;

import kr.hs.entrydsm.husky.domain.application.domain.GEDApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GEDApplicationRepository extends JpaRepository<GEDApplication, Integer> {
    boolean existsByReceiptCode(int receiptCode);
}
