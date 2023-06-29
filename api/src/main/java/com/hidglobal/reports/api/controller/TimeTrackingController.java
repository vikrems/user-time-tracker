package com.hidglobal.reports.api.controller;

import com.hidglobal.reports.api.dto.TimeTrackerDto;
import com.hidglobal.reports.api.service.TimeTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.hidglobal.reports.api.util.Constant.UTC;
import static java.util.Objects.isNull;

@RestController
@RequiredArgsConstructor
public class TimeTrackingController {

    private final TimeTrackingService timeTrackingService;

    @GetMapping("/credHolderId/{credHolderId}/date/{date}")
    public ResponseEntity<TimeTrackerDto> trackTimeWithTimeZone(@PathVariable("credHolderId") String credHolderId,
                                                                @PathVariable("date") String date,
                                                                @RequestParam(value = "timezone", required = false) String timezone) {
        if (isNull(timezone))
            timezone = UTC;
        TimeTrackerDto timeTrackingDto = timeTrackingService.fetchTimeTrackerWithTz(credHolderId, date, timezone);
        return ResponseEntity.ok(timeTrackingDto);
    }
}
