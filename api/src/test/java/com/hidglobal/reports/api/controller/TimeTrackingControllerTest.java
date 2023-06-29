package com.hidglobal.reports.api.controller;

import com.hidglobal.reports.api.service.TimeTrackingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.hidglobal.reports.api.util.Constant.UTC;
import static com.hidglobal.reports.api.util.TestConstants.*;
import static com.hidglobal.reports.api.util.TestUtil.createTimeTrackerDto;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class TimeTrackingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TimeTrackingService timeTrackingService;

    @Test
    public void fetchTimeTracker_TimezoneNotSpecified() throws Exception {
        when(timeTrackingService.fetchTimeTrackerWithTz(CRED_HOLDER_ID, DATE, UTC))
                .thenReturn(createTimeTrackerDto());
        mockMvc
                .perform(get("/credHolderId/{credHolderId}/date/{date}",
                        CRED_HOLDER_ID, DATE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.credHolderName").value(CRED_HOLDER_NAME))
                .andExpect(jsonPath("$.totalDuration").value("0 hour(s), 12 minute(s), 17 second(s)"))
                .andExpect(jsonPath("$.roundTrips[0].inTime").value("2023-06-06T01:29:46"))
                .andExpect(jsonPath("$.roundTrips[0].outTime").value("2023-06-06T01:42:03"))
                .andExpect(jsonPath("$.roundTrips[0].duration").value("0 hour(s), 12 minute(s), 17 second(s)"));
    }

    @Test
    public void fetchTimeTracker_TimezoneSpecified() throws Exception {
        when(timeTrackingService.fetchTimeTrackerWithTz(CRED_HOLDER_ID, DATE, IST))
                .thenReturn(createTimeTrackerDto());
        mockMvc
                .perform(get("/credHolderId/{credHolderId}/date/{date}",
                        CRED_HOLDER_ID, DATE)
                        .param("timezone", IST)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.credHolderName").value(CRED_HOLDER_NAME))
                .andExpect(jsonPath("$.totalDuration").value("0 hour(s), 12 minute(s), 17 second(s)"))
                .andExpect(jsonPath("$.roundTrips[0].inTime").value("2023-06-06T01:29:46"))
                .andExpect(jsonPath("$.roundTrips[0].outTime").value("2023-06-06T01:42:03"))
                .andExpect(jsonPath("$.roundTrips[0].duration").value("0 hour(s), 12 minute(s), 17 second(s)"));
    }
}
