package com.asistec.report.service;

import com.asistec.report.dto.PendingSectionResponse;
import com.asistec.report.dto.SectionSummaryResponse;
import com.asistec.report.dto.StudentAttendanceSummaryResponse;
import com.asistec.report.dto.StudentHistoryResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {


    List<SectionSummaryResponse> getTodaySummary();

    List<PendingSectionResponse> getPendingSections();

    List<StudentHistoryResponse> getStudentHistory(
            Long studentId,
            LocalDate startDate,
            LocalDate endDate
    );
    List<StudentAttendanceSummaryResponse>
    getStudentAttendanceSummary(
            LocalDate startDate,
            LocalDate endDate
    );
}