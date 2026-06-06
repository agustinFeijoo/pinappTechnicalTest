package com.asistec.report.service;

import com.asistec.report.dto.SectionSummaryResponse;

import java.util.List;

public interface ReportService {

    List<SectionSummaryResponse> getTodaySummary();

}