package com.hidglobal.reports.api.util;

import com.hidglobal.reports.api.dto.RoundTripDto;
import com.hidglobal.reports.api.dto.TimeTrackerDto;

import java.util.List;

import static com.hidglobal.reports.api.util.TestConstants.*;
import static com.hidglobal.reports.api.util.TestConstants.CRED_HOLDER_NAME;

public class TestUtil {

    public static TimeTrackerDto createTimeTrackerDto() {
        RoundTripDto roundTripDto = RoundTripDto.createSwipedOutSession(SWIPE_IN_TIME, SWIPE_OUT_TIME, IST);
        return new TimeTrackerDto(CRED_HOLDER_NAME, List.of(roundTripDto));
    }
}
