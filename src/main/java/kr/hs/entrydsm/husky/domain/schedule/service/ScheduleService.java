package kr.hs.entrydsm.husky.domain.schedule.service;

import kr.hs.entrydsm.husky.domain.schedule.dto.CreateScheduleRequest;

public interface ScheduleService {

    String createSchedule(CreateScheduleRequest request, String secret);

}
