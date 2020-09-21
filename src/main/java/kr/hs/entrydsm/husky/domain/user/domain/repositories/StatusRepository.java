package kr.hs.entrydsm.husky.domain.user.domain.repositories;

import kr.hs.entrydsm.husky.domain.user.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
}
