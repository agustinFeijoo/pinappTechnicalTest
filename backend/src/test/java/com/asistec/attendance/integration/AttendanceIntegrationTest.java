package com.asistec.attendance.integration;

import com.asistec.attendance.dto.AttendanceStudentRequest;
import com.asistec.attendance.dto.SaveAttendanceRequest;
import com.asistec.attendance.entity.AttendanceStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AttendanceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSaveAttendanceEndToEnd() throws Exception {

        SaveAttendanceRequest request =
                new SaveAttendanceRequest(
                        List.of(
                                new AttendanceStudentRequest(
                                        1L,
                                        AttendanceStatus.PRESENT
                                )
                        )
                );

        mockMvc.perform(
                        put("/api/sections/1/attendance/today")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper
                                                .writeValueAsString(
                                                        request
                                                )
                                )
                )
                .andExpect(status().isOk());
    }
}