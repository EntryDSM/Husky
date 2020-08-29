package kr.hs.entrydsm.husky.domain.application.domain.repositories;

import kr.hs.entrydsm.husky.domain.application.domain.Application;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationBaseRepository<T extends Application> extends CrudRepository<T, Integer> {
}
