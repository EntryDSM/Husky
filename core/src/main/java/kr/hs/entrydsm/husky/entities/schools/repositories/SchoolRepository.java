package kr.hs.entrydsm.husky.entities.schools.repositories;

import kr.hs.entrydsm.husky.entities.schools.School;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends CrudRepository<School, String> {
}
