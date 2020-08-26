package kr.hs.entrydsm.husky.entities.applications.repositories;

import kr.hs.entrydsm.husky.entities.applications.Application;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationBaseRepository<T extends Application> extends CrudRepository<T, Integer> {
}
