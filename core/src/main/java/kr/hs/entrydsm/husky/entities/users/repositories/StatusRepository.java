package kr.hs.entrydsm.husky.entities.users.repositories;

import kr.hs.entrydsm.husky.entities.users.Status;
import kr.hs.entrydsm.husky.entities.users.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends CrudRepository<Status, String> {
    Optional<Status> findByEmail(String email);
}
