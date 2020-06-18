package kr.hs.entrydsm.husky.entities.schools.repositories;

import kr.hs.entrydsm.husky.entities.schools.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, String> {

}
