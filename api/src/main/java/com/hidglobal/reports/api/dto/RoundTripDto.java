package com.hidglobal.reports.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import static com.hidglobal.reports.api.dto.TimeTrackerDto.Status.SWIPED_OUT;
import static com.hidglobal.reports.common.DateTimeUtils.epochSecondsToStringDate;
import static com.hidglobal.reports.common.DateTimeUtils.getSegmentedDuration;

@Getter
@Setter
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoundTripDto {
    private String inTime;
    private String outTime;
    private String duration;

    @JsonIgnore
    private long durationInEpochSeconds;

    private TimeTrackerDto.Status status;

    private RoundTripDto(long inTime, long outTime, String timeZone) {
        this.inTime = epochSecondsToStringDate(inTime, timeZone);
        this.outTime = epochSecondsToStringDate(outTime, timeZone);
        durationInEpochSeconds = outTime - inTime;
        this.duration = getSegmentedDuration(durationInEpochSeconds);
        this.status = SWIPED_OUT;
    }

    public static RoundTripDto createSwipedOutSession(long inTime, long outTime, String timeZone) {
        return new RoundTripDto(inTime, outTime, timeZone);
    }
}
