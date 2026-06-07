package com.asistec.report.dto;

public record StudentAttendanceSummaryResponse(
        Long studentId,
        String firstName,
        String lastName,
        long presentCount,
        long lateCount,
        long absentCount
) {
}