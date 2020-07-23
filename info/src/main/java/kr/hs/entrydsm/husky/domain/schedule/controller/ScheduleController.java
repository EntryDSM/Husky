package kr.hs.entrydsm.husky.domain.schedule.controller;

import kr.hs.entrydsm.husky.domain.schedule.dto.CreateScheduleRequest;
import kr.hs.entrydsm.husky.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/schedules")
@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public String createSchedule(@RequestBody CreateScheduleRequest request) {
        return scheduleService.createSchedule(request);
    }
}
