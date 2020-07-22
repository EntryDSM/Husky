package kr.hs.entrydsm.husky;

import kr.hs.entrydsm.husky.domain.schedule.domain.Schedule;
import kr.hs.entrydsm.husky.domain.schedule.dao.ScheduleRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InfoApplication.class)
public class ScheduleRepositoryTest {

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
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.of(2020, 8, 30);
        Schedule schedule = Schedule.builder()
                .id(id)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        //when
        scheduleRepository.save(schedule);

        //then
        List<Schedule> savedSchedule = (List<Schedule>) scheduleRepository.findAll();
        assertThat(savedSchedule.get(0).getId()).isEqualTo(id);
        assertThat(savedSchedule.get(0).getStartDate()).isEqualTo(startDate);
        assertThat(savedSchedule.get(0).getEndDate()).isEqualTo(endDate);
    }
    
}
