package com.hidglobal.reports.common;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class DateTimeUtilsTest {

    private static final String START_DATE = "2023-02-04T09:30:00.000Z";
    private static final String NEXT_DATE = "2023-02-05T09:30:00.000Z";
    private static final String END_DATE = "2023-02-06T01:00:00.000Z";
    private static final String DATE = "2023-03-17";
    private static final String DATE_TIME_START_OF_DAY = "2023-03-17T00:00:00";
    private static final String DATE_TIME_IST = "2023-03-17T05:30:00";
    private static final int DAY_AS_EPOCH = 1679011200;
    private static final String INTERMITTENT_FIRST = "2023-02-05T00:00:00.000Z";
    private static final String INTERMITTENT_SECOND = "2023-02-06T00:00:00.000Z";

    @Test
    void findIntermittentDates_Consecutive() {
        List<String> dates = DateTimeUtils.findIntermittentDates(START_DATE, NEXT_DATE);
        assertEquals(1, dates.size());
        assertEquals(INTERMITTENT_FIRST, dates.get(0));
    }

    @Test
    void findIntermittentDates_NotConsecutive() {
        List<String> dates = DateTimeUtils.findIntermittentDates(START_DATE, END_DATE);
        assertEquals(2, dates.size());
        assertEquals(INTERMITTENT_FIRST, dates.get(0));
        assertEquals(INTERMITTENT_SECOND, dates.get(1));
    }

    @Test
    void findIntermittentDates_SameDate() {
        List<String> dates = DateTimeUtils.findIntermittentDates(START_DATE, START_DATE);
        assertTrue(dates.isEmpty());
    }

    @Test
    void startTimeAsEpochWithTimeZone_IST() {
        long timeInMillis = DateTimeUtils.startTimeAsEpochWithTimeZone(DATE, "UTC");
        assertEquals(DAY_AS_EPOCH, timeInMillis);
    }

    @Test
    void epochToStringDateWithTimeZone_UTC() {
        String day = DateTimeUtils.epochSecondsToStringDate(DAY_AS_EPOCH, "UTC");
        assertEquals(DATE_TIME_START_OF_DAY, day);
    }

    @Test
    void epochToStringDateWithTimeZone_IST() {
        String day = DateTimeUtils.epochSecondsToStringDate(DAY_AS_EPOCH, "Asia/Kolkata");
        assertEquals(DATE_TIME_IST, day);
    }

    @Test
    void epochToStringDateWithStartOfDay_IST() {
        String day = DateTimeUtils.epochToStringDateWithStartOfDay(DAY_AS_EPOCH, "Asia/Kolkata");
        assertEquals(DATE_TIME_START_OF_DAY, day);
    }
}
