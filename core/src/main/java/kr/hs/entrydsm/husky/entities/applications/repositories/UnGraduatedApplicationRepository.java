package kr.hs.entrydsm.husky.entities.applications.repositories;

import kr.hs.entrydsm.husky.entities.applications.UnGraduatedApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnGraduatedApplicationRepository extends JpaRepository<UnGraduatedApplication, Integer> {
}
