package kr.hs.entrydsm.husky.domain.schedule.controller;

import kr.hs.entrydsm.husky.domain.schedule.dao.ScheduleRepository;
import kr.hs.entrydsm.husky.domain.schedule.domain.Schedule;
import kr.hs.entrydsm.husky.domain.schedule.dto.CreateScheduleRequest;
import kr.hs.entrydsm.husky.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/schedules")
@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    private final ScheduleRepository scheduleRepository;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public String createSchedule(@RequestBody CreateScheduleRequest request,
                                 @RequestHeader("secret") String secret) {
        return scheduleService.createSchedule(request, secret);
    }

    @GetMapping
    public List<Schedule> getSchedules() {
        return scheduleRepository.findAllByOrderByEndDate();
    }
}
