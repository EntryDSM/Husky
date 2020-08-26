package kr.hs.entrydsm.husky.entities.applications.repositories;

import kr.hs.entrydsm.husky.entities.applications.GraduatedApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraduatedApplicationRepository extends JpaRepository<GraduatedApplication, Integer> {
}
