package kr.hs.entrydsm.husky.entities.users.repositories;

import kr.hs.entrydsm.husky.entities.users.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByReceiptCode(int receiptCode);

}
