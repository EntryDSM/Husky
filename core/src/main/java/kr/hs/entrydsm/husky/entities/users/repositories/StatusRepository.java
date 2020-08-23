package kr.hs.entrydsm.husky.entities.users.repositories;

import kr.hs.entrydsm.husky.entities.users.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends CrudRepository<Status, String> {
}
