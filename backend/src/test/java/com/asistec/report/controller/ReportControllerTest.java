package com.asistec.report.controller;

import com.asistec.report.service.ReportService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReportService reportService;

    @Test
    void getTodaySummary_shouldReturn200()
            throws Exception {

        mockMvc.perform(
                        get(
                                "/api/reports/summary/today"
                        )
                )
                .andExpect(status().isOk());
    }

    @Test
    void getStudentHistory_shouldReturn200()
            throws Exception {

        mockMvc.perform(
                        get(
                                "/api/reports/students/1/history"
                        )
                                .param(
                                        "startDate",
                                        "2026-01-01"
                                )
                                .param(
                                        "endDate",
                                        "2026-12-31"
                                )
                )
                .andExpect(status().isOk());
    }
}