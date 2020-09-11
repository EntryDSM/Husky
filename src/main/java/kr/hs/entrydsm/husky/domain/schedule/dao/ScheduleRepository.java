package kr.hs.entrydsm.husky.domain.schedule.dao;

import kr.hs.entrydsm.husky.domain.schedule.domain.Schedule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends CrudRepository<Schedule, String> {
    List<Schedule> findAllByOrderByEndDate();
}
