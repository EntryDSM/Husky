package kr.hs.entrydsm.husky.entities.applications.repositories;

import kr.hs.entrydsm.husky.entities.applications.GEDApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GEDApplicationRepository extends JpaRepository<GEDApplication, Integer> {
}
