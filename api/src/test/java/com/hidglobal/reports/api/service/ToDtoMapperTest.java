package com.hidglobal.reports.api.service;

import com.hidglobal.reports.api.dto.RoundTripDto;
import com.hidglobal.reports.api.dto.TimeTrackerDto;
import com.hidglobal.reports.persistence.entity.RoundTripEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.hidglobal.reports.api.util.Constant.UTC;
import static com.hidglobal.reports.api.util.TestConstants.*;
import static com.hidglobal.reports.persistence.entity.EventType.ALLOWED_NORMAL_IN;
import static com.hidglobal.reports.persistence.entity.EventType.ALLOWED_NORMAL_OUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ToDtoMapperTest {

    private static final long FIRST_SWIPE_IN = 1635567174;
    private static final long FIRST_SWIPE_OUT = 1635570957;
    private static final long SECOND_SWIPE_IN = 1635571201;
    private static final long SECOND_SWIPE_OUT = 1635595477;
    private static final long SWIPE_OUT_NEXT_DAY = 1635665415;
    private final ToDtoMapper toDtoMapper = new ToDtoMapper();

    @Test
    public void fetchTimeSegments_startAndEndOnSameDay_UTC() {
        //Preparation
        RoundTripEntity firstSwipeInEntity = new RoundTripEntity(CRED_HOLDER_ID, FIRST_SWIPE_IN,
                CRED_HOLDER_NAME, ALLOWED_NORMAL_IN);
        RoundTripEntity firstSwipeOutEntity = new RoundTripEntity(CRED_HOLDER_ID, FIRST_SWIPE_OUT,
                CRED_HOLDER_NAME, ALLOWED_NORMAL_OUT);
        RoundTripEntity secondSwipeInEntity = new RoundTripEntity(CRED_HOLDER_ID, SECOND_SWIPE_IN,
                CRED_HOLDER_NAME, ALLOWED_NORMAL_IN);
        RoundTripEntity secondSwipeOutEntity = new RoundTripEntity(CRED_HOLDER_ID, SECOND_SWIPE_OUT,
                CRED_HOLDER_NAME, ALLOWED_NORMAL_OUT);
        List<RoundTripEntity> roundTripEntities =
                List.of(
                        firstSwipeInEntity,
                        firstSwipeOutEntity,
                        secondSwipeInEntity,
                        secondSwipeOutEntity);

        //Invocation
        TimeTrackerDto timeTrackerDto = toDtoMapper.fetchTimeSegments(roundTripEntities, UTC);

        //Assertions
        assertNotNull(timeTrackerDto);
        List<RoundTripDto> roundTripDtos = timeTrackerDto.getRoundTrips();
        assertEquals(CRED_HOLDER_NAME, timeTrackerDto.getCredHolderName());
        assertEquals("7 hour(s), 47 minute(s), 39 second(s)", timeTrackerDto.getTotalDuration());
        assertEquals(2, roundTripDtos.size());
        RoundTripDto firstRoundTripDto = roundTripDtos.get(0);
        assertEquals("2021-10-30T04:12:54", firstRoundTripDto.getInTime());
        assertEquals("2021-10-30T05:15:57", firstRoundTripDto.getOutTime());
        assertEquals("1 hour(s), 3 minute(s), 3 second(s)", firstRoundTripDto.getDuration());

        RoundTripDto roundTripDto = roundTripDtos.get(1);
        assertEquals("2021-10-30T05:20:01", roundTripDto.getInTime());
        assertEquals("2021-10-30T12:04:37", roundTripDto.getOutTime());
        assertEquals("6 hour(s), 44 minute(s), 36 second(s)", roundTripDto.getDuration());
    }

    @Test
    public void fetchTimeSegments_startAndEndOnSameDay_IST() {
        //Preparation
        RoundTripEntity firstSwipeInEntity = new RoundTripEntity(CRED_HOLDER_ID, FIRST_SWIPE_IN,
                CRED_HOLDER_NAME, ALLOWED_NORMAL_IN);
        RoundTripEntity firstSwipeOutEntity = new RoundTripEntity(CRED_HOLDER_ID, FIRST_SWIPE_OUT,
                CRED_HOLDER_NAME, ALLOWED_NORMAL_OUT);
        RoundTripEntity secondSwipeInEntity = new RoundTripEntity(CRED_HOLDER_ID, SECOND_SWIPE_IN,
                CRED_HOLDER_NAME, ALLOWED_NORMAL_IN);
        RoundTripEntity secondSwipeOutEntity = new RoundTripEntity(CRED_HOLDER_ID, SECOND_SWIPE_OUT,
                CRED_HOLDER_NAME, ALLOWED_NORMAL_OUT);
        List<RoundTripEntity> roundTripEntities =
                List.of(
                        firstSwipeInEntity,
                        firstSwipeOutEntity,
                        secondSwipeInEntity,
                        secondSwipeOutEntity);

        //Invocation
        TimeTrackerDto timeTrackerDto = toDtoMapper.fetchTimeSegments(roundTripEntities, IST);

        //Assertions
        assertNotNull(timeTrackerDto);
        List<RoundTripDto> roundTripDtos = timeTrackerDto.getRoundTrips();
        assertEquals(CRED_HOLDER_NAME, timeTrackerDto.getCredHolderName());
        assertEquals("7 hour(s), 47 minute(s), 39 second(s)", timeTrackerDto.getTotalDuration());
        assertEquals(2, roundTripDtos.size());
        RoundTripDto firstRoundTripDto = roundTripDtos.get(0);
        assertEquals("2021-10-30T09:42:54", firstRoundTripDto.getInTime());
        assertEquals("2021-10-30T10:45:57", firstRoundTripDto.getOutTime());
        assertEquals("1 hour(s), 3 minute(s), 3 second(s)", firstRoundTripDto.getDuration());

        RoundTripDto roundTripDto = roundTripDtos.get(1);
        assertEquals("2021-10-30T10:50:01", roundTripDto.getInTime());
        assertEquals("2021-10-30T17:34:37", roundTripDto.getOutTime());
        assertEquals("6 hour(s), 44 minute(s), 36 second(s)", roundTripDto.getDuration());
    }

    @Test
    public void fetchTimeSegments_NoStartButEndOnSameDay_IST() {
        RoundTripEntity firstSwipeOutEntity = new RoundTripEntity(CRED_HOLDER_ID, FIRST_SWIPE_OUT,
                CRED_HOLDER_NAME, ALLOWED_NORMAL_OUT);
        RoundTripEntity secondSwipeInEntity = new RoundTripEntity(CRED_HOLDER_ID, SECOND_SWIPE_IN,
                CRED_HOLDER_NAME, ALLOWED_NORMAL_IN);
        RoundTripEntity secondSwipeOutEntity = new RoundTripEntity(CRED_HOLDER_ID, SECOND_SWIPE_OUT,
                CRED_HOLDER_NAME, ALLOWED_NORMAL_OUT);
        List<RoundTripEntity> roundTripEntities =
                List.of(
                        firstSwipeOutEntity,
                        secondSwipeInEntity,
                        secondSwipeOutEntity);

        //Invocation
        TimeTrackerDto timeTrackerDto = toDtoMapper.fetchTimeSegments(roundTripEntities, IST);

        //Assertions
        assertNotNull(timeTrackerDto);
        List<RoundTripDto> roundTripDtos = timeTrackerDto.getRoundTrips();
        assertEquals(CRED_HOLDER_NAME, timeTrackerDto.getCredHolderName());
        assertEquals("17 hour(s), 30 minute(s), 33 second(s)", timeTrackerDto.getTotalDuration());
        assertEquals(2, roundTripDtos.size());
        RoundTripDto firstRoundTripDto = roundTripDtos.get(0);
        assertEquals("2021-10-30T00:00:00", firstRoundTripDto.getInTime());
        assertEquals("2021-10-30T10:45:57", firstRoundTripDto.getOutTime());
        assertEquals("10 hour(s), 45 minute(s), 57 second(s)", firstRoundTripDto.getDuration());

        RoundTripDto roundTripDto = roundTripDtos.get(1);
        assertEquals("2021-10-30T10:50:01", roundTripDto.getInTime());
        assertEquals("2021-10-30T17:34:37", roundTripDto.getOutTime());
        assertEquals("6 hour(s), 44 minute(s), 36 second(s)", roundTripDto.getDuration());
    }

    @Test
    public void fetchTimeSegments_startOnSameDayButNoEnd() {
        //Preparation
        RoundTripEntity firstSwipeInEntity = new RoundTripEntity(CRED_HOLDER_ID, FIRST_SWIPE_IN,
                CRED_HOLDER_NAME, ALLOWED_NORMAL_IN);
        RoundTripEntity firstSwipeOutEntity = new RoundTripEntity(CRED_HOLDER_ID, FIRST_SWIPE_OUT,
                CRED_HOLDER_NAME, ALLOWED_NORMAL_OUT);
        RoundTripEntity secondSwipeInEntity = new RoundTripEntity(CRED_HOLDER_ID, SECOND_SWIPE_IN,
                CRED_HOLDER_NAME, ALLOWED_NORMAL_IN);
        List<RoundTripEntity> roundTripEntities =
                List.of(
                        firstSwipeInEntity,
                        firstSwipeOutEntity,
                        secondSwipeInEntity);

        //Invocation
        TimeTrackerDto timeTrackerDto = toDtoMapper.fetchTimeSegments(roundTripEntities, IST);

        //Assertions
        assertNotNull(timeTrackerDto);
        List<RoundTripDto> roundTripDtos = timeTrackerDto.getRoundTrips();
        assertEquals(CRED_HOLDER_NAME, timeTrackerDto.getCredHolderName());
        assertEquals("14 hour(s), 13 minute(s), 1 second(s)", timeTrackerDto.getTotalDuration());
        assertEquals(2, roundTripDtos.size());
        RoundTripDto firstRoundTripDto = roundTripDtos.get(0);
        assertEquals("2021-10-30T09:42:54", firstRoundTripDto.getInTime());
        assertEquals("2021-10-30T10:45:57", firstRoundTripDto.getOutTime());
        assertEquals("1 hour(s), 3 minute(s), 3 second(s)", firstRoundTripDto.getDuration());

        RoundTripDto roundTripDto = roundTripDtos.get(1);
        assertEquals("2021-10-30T10:50:01", roundTripDto.getInTime());
        assertEquals("2021-10-30T23:59:59", roundTripDto.getOutTime());
        assertEquals("13 hour(s), 9 minute(s), 58 second(s)", roundTripDto.getDuration());
    }

    @Test
    public void fetchTimeSegments_endTimeNextDay_UTC() {
        //Preparation
        RoundTripEntity firstSwipeInEntity = new RoundTripEntity(CRED_HOLDER_ID, FIRST_SWIPE_IN,
                CRED_HOLDER_NAME, ALLOWED_NORMAL_IN);
        RoundTripEntity firstSwipeOutEntity = new RoundTripEntity(CRED_HOLDER_ID, FIRST_SWIPE_OUT,
                CRED_HOLDER_NAME, ALLOWED_NORMAL_OUT);
        RoundTripEntity secondSwipeInEntity = new RoundTripEntity(CRED_HOLDER_ID, SECOND_SWIPE_IN,
                CRED_HOLDER_NAME, ALLOWED_NORMAL_IN);
        RoundTripEntity secondSwipeOutEntity = new RoundTripEntity(CRED_HOLDER_ID, SWIPE_OUT_NEXT_DAY,
                CRED_HOLDER_NAME, ALLOWED_NORMAL_OUT);
        List<RoundTripEntity> roundTripEntities =
                List.of(
                        firstSwipeInEntity,
                        firstSwipeOutEntity,
                        secondSwipeInEntity,
                        secondSwipeOutEntity);

        //Invocation
        TimeTrackerDto timeTrackerDto = toDtoMapper.fetchTimeSegments(roundTripEntities, UTC);

        //Assertions
        assertNotNull(timeTrackerDto);
        List<RoundTripDto> roundTripDtos = timeTrackerDto.getRoundTrips();
        assertEquals(CRED_HOLDER_NAME, timeTrackerDto.getCredHolderName());
        assertEquals("27 hour(s), 13 minute(s), 17 second(s)", timeTrackerDto.getTotalDuration());
        assertEquals(2, roundTripDtos.size());
        RoundTripDto firstRoundTripDto = roundTripDtos.get(0);
        assertEquals("2021-10-30T04:12:54", firstRoundTripDto.getInTime());
        assertEquals("2021-10-30T05:15:57", firstRoundTripDto.getOutTime());
        assertEquals("1 hour(s), 3 minute(s), 3 second(s)", firstRoundTripDto.getDuration());

        RoundTripDto roundTripDto = roundTripDtos.get(1);
        assertEquals("2021-10-30T05:20:01", roundTripDto.getInTime());
        assertEquals("2021-10-31T07:30:15", roundTripDto.getOutTime());
        assertEquals("26 hour(s), 10 minute(s), 14 second(s)", roundTripDto.getDuration());
    }
}
