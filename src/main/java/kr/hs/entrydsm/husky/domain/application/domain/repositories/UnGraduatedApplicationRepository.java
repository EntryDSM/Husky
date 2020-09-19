package kr.hs.entrydsm.husky.domain.application.domain.repositories;

import kr.hs.entrydsm.husky.domain.application.domain.UnGraduatedApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnGraduatedApplicationRepository extends JpaRepository<UnGraduatedApplication, Integer> {
    boolean existsByReceiptCode(int receiptCode);
}
