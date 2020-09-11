package hs.kr.entrydsm.husky.domain.info;

import kr.hs.entrydsm.husky.infra.redis.EmbeddedRedisConfig;
import kr.hs.entrydsm.husky.HuskyApplication;
import kr.hs.entrydsm.husky.domain.schedule.domain.Schedule;
import kr.hs.entrydsm.husky.domain.schedule.dao.ScheduleRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {HuskyApplication.class, EmbeddedRedisConfig.class})
@ActiveProfiles({"test", "local"})
class ScheduleRepositoryTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @After
    public void tearDown() throws Exception {
        scheduleRepository.deleteAll();
    }

    @Test
    public void save_and_find() {
        //given
        String id = "엔트리 개발";
        LocalDateTime startDate = LocalDateTime.of(2020, 8,29,8, 30);
        LocalDateTime endDate = LocalDateTime.of(2020, 8, 30, 9, 40);
        Schedule schedule = Schedule.builder()
                .id(id)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        //when
        scheduleRepository.save(schedule);

        //then
        List<Schedule> savedSchedule = (List<Schedule>) scheduleRepository.findAll();
        assertEquals(savedSchedule.get(0).getId(), id);
        assertEquals(savedSchedule.get(0).getStartDate(), startDate);
        assertEquals(savedSchedule.get(0).getEndDate(), endDate);
    }
    
}
