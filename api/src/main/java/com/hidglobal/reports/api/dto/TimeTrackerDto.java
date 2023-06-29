package com.hidglobal.reports.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.hidglobal.reports.common.DateTimeUtils.getSegmentedDuration;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TimeTrackerDto {

    private String credHolderName;
    private String totalDuration;
    private List<RoundTripDto> roundTrips;

    public static TimeTrackerDto createEmptyDto() {
        return new TimeTrackerDto();
    }

    public TimeTrackerDto(String credHolderName, List<RoundTripDto> roundTrips) {
        this.credHolderName = credHolderName;
        long totalDurationMillis = roundTrips
                .stream()
                .map(RoundTripDto::getDurationInEpochSeconds)
                .mapToLong(Long::longValue)
                .sum();
        this.totalDuration = getSegmentedDuration(totalDurationMillis);
        this.roundTrips = roundTrips;
    }

    public enum Status {
        SWIPED_OUT,
        INSIDE;
    }

    public void addToRoundTrips(RoundTripDto roundTrip) {
        roundTrips.add(roundTrip);
    }
}
