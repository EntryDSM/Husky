package kr.hs.entrydsm.husky.domain.application.domain.repositories;

import kr.hs.entrydsm.husky.domain.application.domain.CalculatedScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CalculatedScoreRepository extends JpaRepository<CalculatedScore, Integer> {
}
