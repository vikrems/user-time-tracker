package com.hidglobal.reports.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import static com.hidglobal.reports.common.ApiTestConstant.*;
import static java.time.temporal.ChronoField.INSTANT_SECONDS;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.SECONDS;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtils {

    public static List<String> findIntermittentDates(String startDateTime, String endDateTime) {
        ZonedDateTime zonedStart = zonedDateTimefromString(startDateTime, ISO_FORMATTER);
        ZonedDateTime zonedEnd = zonedDateTimefromString(endDateTime, ISO_FORMATTER);
        List<String> intermittentDates = new LinkedList<>();
        ZonedDateTime currentDate = startOfDayTime(zonedStart);
        while (currentDate.getDayOfYear() != zonedEnd.getDayOfYear()) {
            currentDate = currentDate.plusDays(1);
            intermittentDates.add(currentDate.format(ISO_FORMATTER));
        }
        return intermittentDates;
    }

    public static long calculateDuration(String startDateTime, String endDateTime) {
        ZonedDateTime zonedStartTime = zonedDateTimefromString(startDateTime, ISO_FORMATTER);
        ZonedDateTime zonedEndTime = zonedDateTimefromString(endDateTime, ISO_FORMATTER);
        return Duration.between(zonedStartTime, zonedEndTime).toMillis();
    }

    private static ZonedDateTime startOfDayTime(ZonedDateTime zonedStart) {
        return ZonedDateTime.from(zonedStart)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .truncatedTo(SECONDS);
    }

    public static String timeStampWithEndOfDate(String startDateTime) {
        ZonedDateTime zonedStartTime = zonedDateTimefromString(startDateTime, ISO_FORMATTER);
        ZonedDateTime timeAtEndOfDate = endOfDayTime(zonedStartTime);
        return timeAtEndOfDate.format(ISO_FORMATTER);
    }

    public static ZonedDateTime endOfDayTime(ZonedDateTime zonedStart) {
        return ZonedDateTime.from(zonedStart)
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .truncatedTo(SECONDS);
    }

    public static boolean isSameDay(String startDateTime, String endDateTime) {
        ZonedDateTime zonedStartTime = zonedDateTimefromString(startDateTime, ISO_FORMATTER);
        ZonedDateTime zonedEndTime = zonedDateTimefromString(endDateTime, ISO_FORMATTER);
        return zonedStartTime.getDayOfYear() == zonedEndTime.getDayOfYear();
    }

    public static ZonedDateTime zonedDateTimefromString(String dateTime,
                                                        DateTimeFormatter dateTimeFormatter) {

        return ZonedDateTime.parse(dateTime, dateTimeFormatter)
                .truncatedTo(SECONDS);
    }

    public static ZonedDateTime zonedDateTimeInEpochfromString(String dateTime) {
        return ZonedDateTime.parse(dateTime, ISO_FORMATTER)
                .truncatedTo(MILLIS);
    }

    public static long startTimeAsEpochWithTimeZone(String date, String timezone) {
        LocalDate localDate = LocalDate.parse(date, DATE_FORMATTER);
        ZonedDateTime startDateTime = localDate.atStartOfDay(ZoneId.of(timezone));
        return startDateTime.toInstant().getEpochSecond();
    }

    public static String epochSecondsToStringDate(long epochInSeconds, String timezone) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(epochInSeconds),
                ZoneId.of(timezone));
        return zdt.format(TIME_FORMATTER);
    }

    public static String epochMillisToStringDate(long epochInSeconds, String timezone) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(epochInSeconds),
                ZoneId.of(timezone));
        return zdt.format(TIME_FORMATTER);
    }

    public static long epochAtTimeZone(long epochInSeconds, String timezone) {
       return ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(epochInSeconds),
                ZoneId.of(timezone))
                .toInstant()
                .getEpochSecond();
    }

    public static String epochToStringDateWithStartOfDay(long epochInSeconds, String timezone) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(epochInSeconds),
                ZoneId.of(timezone))
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .truncatedTo(SECONDS);

        return zdt.format(TIME_FORMATTER);
    }

    public static long epochWithStartOfDay(long epochInSeconds, String timezone) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(epochInSeconds),
                ZoneId.of(timezone))
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .truncatedTo(SECONDS);

        return zdt.getLong(INSTANT_SECONDS);
    }

    public static long epochWithEndOfDay(long epochInSeconds, String timezone) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(epochInSeconds),
                ZoneId.of(timezone))
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .truncatedTo(SECONDS);

        return zdt.getLong(INSTANT_SECONDS);
    }

    public static String getSegmentedDuration(long epochSeconds) {
        Duration duration = Duration.ofSeconds(epochSeconds);
        long hours = duration.toHours();
        long minutes = duration.toMinutes();
        long seconds = duration.getSeconds();

        seconds = reduceByTimeUnit(minutes, seconds);
        minutes = reduceByTimeUnit(hours, minutes);

        return hours + " hour(s), "
                + minutes + " minute(s), "
                + seconds + " second(s)";
    }

    private static long reduceByTimeUnit(long baseUnit, long childUnit) {
        if (baseUnit != 0)
            childUnit = childUnit % (60 * baseUnit);
        return childUnit;
    }
}