package com.asistec.integration;

import com.asistec.attendance.dto.AttendanceStudentRequest;
import com.asistec.attendance.dto.SaveAttendanceRequest;
import com.asistec.attendance.entity.AttendanceStatus;
import com.asistec.attendance.service.AttendanceService;
import com.asistec.report.service.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AttendanceFlowIntegrationTest {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private ReportService reportService;

    @Test
    void attendanceShouldAppearInReports() {

        SaveAttendanceRequest request =
                new SaveAttendanceRequest(
                        List.of(
                                new AttendanceStudentRequest(
                                        1L,
                                        AttendanceStatus.PRESENT
                                )
                        )
                );

        attendanceService.saveAttendance(
                1L,
                request
        );

        var history =
                reportService.getStudentHistory(
                        1L,
                        LocalDate.now(),
                        LocalDate.now()
                );

        assertNotNull(history);
    }

    @Test
    void studentsSummaryShouldLoad() {

        var summary =
                reportService.getStudentsSummary(
                        LocalDate.of(
                                LocalDate.now()
                                        .getYear(),
                                1,
                                1
                        ),
                        LocalDate.now()
                );

        assertNotNull(summary);
    }
}