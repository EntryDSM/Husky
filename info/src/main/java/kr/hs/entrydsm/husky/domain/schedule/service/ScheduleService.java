package kr.hs.entrydsm.husky.domain.schedule.service;

import kr.hs.entrydsm.husky.domain.schedule.dao.ScheduleRepository;
import kr.hs.entrydsm.husky.domain.schedule.domain.Schedule;
import kr.hs.entrydsm.husky.domain.schedule.dto.CreateScheduleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public String createSchedule(CreateScheduleRequest request) {
        Schedule schedule = scheduleRepository.save(
                Schedule.builder()
                        .id(request.getId())
                        .startDate(request.getStartDate())
                        .endDate(request.getEndDate())
                        .build()
        );

        return schedule.getId();
    }

}
