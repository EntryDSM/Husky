package kr.hs.entrydsm.husky.domain.schedule.service;

import kr.hs.entrydsm.husky.domain.schedule.dao.ScheduleRepository;
import kr.hs.entrydsm.husky.domain.schedule.domain.Schedule;
import kr.hs.entrydsm.husky.domain.schedule.dto.CreateScheduleRequest;
import kr.hs.entrydsm.husky.domain.schedule.exception.NotAdminException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Value("${auth.maintenance.key}")
    private String maintenanceKey;

    @Override
    public String createSchedule(CreateScheduleRequest request, String secret) {
        byte[] targetBytes = secret.getBytes();
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodeBytes = encoder.encode(targetBytes);

        if (!maintenanceKey.equals(new String(encodeBytes))) {
            throw new NotAdminException();
        }

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
