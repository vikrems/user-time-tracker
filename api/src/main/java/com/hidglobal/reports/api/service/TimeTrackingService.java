package com.hidglobal.reports.api.service;

import com.hidglobal.reports.api.dto.TimeTrackerDto;
import com.hidglobal.reports.persistence.entity.RoundTripEntity;
import com.hidglobal.reports.persistence.repository.RoundTripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.hidglobal.reports.api.dto.TimeTrackerDto.createEmptyDto;
import static com.hidglobal.reports.common.DateTimeUtils.startTimeAsEpochWithTimeZone;

@Service
@RequiredArgsConstructor
public class TimeTrackingService {

    private final RoundTripRepository roundTripRepository;
    private final ToDtoMapper toDtoMapper;
    private static final long ONE_DAY = TimeUnit.DAYS.toSeconds(1) - 1; //Reducing by 1 as we want to take the last second of the current day

    public TimeTrackerDto fetchTimeTrackerWithTz(String credHolderId, String date, String timezone) {
        long startTime = startTimeAsEpochWithTimeZone(date, timezone);
        long endTime = startTime + ONE_DAY;
        List<RoundTripEntity> timeTrackerEntities = roundTripRepository
                .findByCredHolderIdBetween(credHolderId, String.valueOf(startTime),
                        String.valueOf(endTime));
        if (timeTrackerEntities.isEmpty())
            return createEmptyDto();
        return toDtoMapper.fetchTimeSegments(timeTrackerEntities, timezone);
    }
}
