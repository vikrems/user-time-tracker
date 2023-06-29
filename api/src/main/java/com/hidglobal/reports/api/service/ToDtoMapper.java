package com.hidglobal.reports.api.service;

import com.hidglobal.reports.api.dto.RoundTripDto;
import com.hidglobal.reports.api.dto.TimeTrackerDto;
import com.hidglobal.reports.persistence.entity.RoundTripEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.hidglobal.reports.common.DateTimeUtils.*;
import static com.hidglobal.reports.persistence.entity.EventType.ALLOWED_NORMAL_IN;
import static com.hidglobal.reports.persistence.entity.EventType.ALLOWED_NORMAL_OUT;

@Service
public class ToDtoMapper {

    public TimeTrackerDto fetchTimeSegments(List<RoundTripEntity> roundTripEntities, String timezone) {
        RoundTripEntity firstEntity = roundTripEntities.get(0);
        long startDateTime = determineStartDateTime(firstEntity, timezone);
        List<RoundTripDto> resultantRoundTrips = new ArrayList<>();
        boolean expectedSwipeOut = true;
        for (RoundTripEntity eachEntity : roundTripEntities) {
            if (expectedSwipeOut && eachEntity.getSwipeType() == ALLOWED_NORMAL_OUT) {
                RoundTripDto roundTrip = RoundTripDto.createSwipedOutSession(startDateTime,
                        eachEntity.getTimestamp(), timezone);
                resultantRoundTrips.add(roundTrip);
                expectedSwipeOut = false;
            } else if (!expectedSwipeOut && eachEntity.getSwipeType() == ALLOWED_NORMAL_IN) {
                startDateTime = epochAtTimeZone(eachEntity.getTimestamp(), timezone);
                expectedSwipeOut = true;
            }
        }
        addAnEndEventIfMissing(timezone, startDateTime, resultantRoundTrips, expectedSwipeOut);
        return new TimeTrackerDto(firstEntity.getCredHolderName(), resultantRoundTrips);
    }

    private void addAnEndEventIfMissing(String timezone, long startDateTime,
                                        List<RoundTripDto> resultantRoundTrips,
                                        boolean expectedSwipeout) {
        if (expectedSwipeout) {
            long endDateTime = epochWithEndOfDay(startDateTime, timezone);
            RoundTripDto roundTrip = RoundTripDto.createSwipedOutSession(startDateTime,
                    endDateTime, timezone);
            resultantRoundTrips.add(roundTrip);
        }
    }

    private long determineStartDateTime(RoundTripEntity roundTripEntity, String timezone) {
        if (roundTripEntity.getSwipeType() == ALLOWED_NORMAL_IN)
            return roundTripEntity.getTimestamp();

        return epochWithStartOfDay(roundTripEntity.getTimestamp(), timezone);
    }
}
