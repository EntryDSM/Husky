package kr.hs.entrydsm.husky.entities.applications.repositories;

import kr.hs.entrydsm.husky.entities.applications.CalculatedScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CalculatedScoreRepository extends JpaRepository<CalculatedScore, Integer> {
}
