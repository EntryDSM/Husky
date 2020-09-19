package kr.hs.entrydsm.husky.domain.application.domain.repositories;

import kr.hs.entrydsm.husky.domain.application.domain.GraduatedApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraduatedApplicationRepository extends JpaRepository<GraduatedApplication, Integer> {
    boolean existsByReceiptCode(int receiptCode);
}
