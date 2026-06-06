package com.asistec.report.controller;

import com.asistec.report.dto.SectionSummaryResponse;
import com.asistec.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}