package com.hidglobal.reports.api.service;

import com.hidglobal.reports.api.dto.TimeTrackerDto;
import com.hidglobal.reports.persistence.entity.RoundTripEntity;
import com.hidglobal.reports.persistence.repository.RoundTripRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.hidglobal.reports.api.util.TestConstants.*;
import static com.hidglobal.reports.api.util.TestUtil.createTimeTrackerDto;
import static com.hidglobal.reports.persistence.entity.EventType.ALLOWED_NORMAL_IN;
import static com.hidglobal.reports.persistence.entity.EventType.ALLOWED_NORMAL_OUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TimeTrackingServiceTest {
    @Mock
    private RoundTripRepository roundTripRepository;
    @Mock
    private ToDtoMapper toDtoMapper;
    @InjectMocks
    private TimeTrackingService timeTrackingService;

    @Test
    public void dataAvailableInDb() {
        List<RoundTripEntity> entitiesList = createRoundTripEntities();
        when(roundTripRepository
                .findByCredHolderIdBetween(CRED_HOLDER_ID, String.valueOf(START_OF_DAY),
                        String.valueOf(END_OF_DAY)))
                .thenReturn(entitiesList);

        TimeTrackerDto expectedResult = createTimeTrackerDto();
        when(toDtoMapper.fetchTimeSegments(entitiesList, IST))
                .thenReturn(expectedResult);

        TimeTrackerDto actualResult = timeTrackingService.fetchTimeTrackerWithTz(CRED_HOLDER_ID, DATE, IST);
        assertEquals(expectedResult, actualResult);
    }

    private List<RoundTripEntity> createRoundTripEntities() {
        return List.of(
                RoundTripEntity.builder()
                        .credentialHolderId(CRED_HOLDER_ID)
                        .credHolderName(CRED_HOLDER_NAME)
                        .swipeType(ALLOWED_NORMAL_IN)
                        .timestamp(SWIPE_IN_TIME) //2023-06-06 01:29:46
                        .build(),
                RoundTripEntity.builder()
                        .credentialHolderId(CRED_HOLDER_ID)
                        .credHolderName(CRED_HOLDER_NAME)
                        .swipeType(ALLOWED_NORMAL_OUT)
                        .timestamp(SWIPE_OUT_TIME) //2023-06-06 01:42:03
                        .build());
    }

    @Test
    public void dataNotAvailableInDb() {
        when(roundTripRepository
                .findByCredHolderIdBetween(CRED_HOLDER_ID, String.valueOf(START_OF_DAY),
                        String.valueOf(END_OF_DAY)))
                .thenReturn(List.of());

        TimeTrackerDto actualResult = timeTrackingService.fetchTimeTrackerWithTz(CRED_HOLDER_ID, DATE, IST);

        TimeTrackerDto expectedResult = TimeTrackerDto.createEmptyDto();
        assertEquals(expectedResult, actualResult);
        verifyNoInteractions(toDtoMapper);
    }
}
