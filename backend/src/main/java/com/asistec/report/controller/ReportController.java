package com.asistec.report.controller;

import com.asistec.report.dto.PendingSectionResponse;
import com.asistec.report.dto.SectionSummaryResponse;
import com.asistec.report.dto.StudentAttendanceSummaryResponse;
import com.asistec.report.dto.StudentHistoryResponse;
import com.asistec.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/summary/today")
    public List<SectionSummaryResponse>
    getTodaySummary() {

        return reportService.getTodaySummary();
    }

    @GetMapping("/pending-sections")
    public List<PendingSectionResponse> getPendingSections() {
        return reportService.getPendingSections();
    }

    @GetMapping("/summary")
    public List<SectionSummaryResponse> getSummary(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return reportService.getTodaySummary();
    }

    @GetMapping("/students/{studentId}/history")
    public List<StudentHistoryResponse> getStudentHistory(

            @PathVariable
            Long studentId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {

        return reportService.getStudentHistory(
                studentId,
                startDate,
                endDate
        );
    }

    @GetMapping("/students-summary")
    public List<StudentAttendanceSummaryResponse>
    getStudentsSummary(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {

        return reportService
                .getStudentAttendanceSummary(
                        startDate,
                        endDate
                );
    }
}