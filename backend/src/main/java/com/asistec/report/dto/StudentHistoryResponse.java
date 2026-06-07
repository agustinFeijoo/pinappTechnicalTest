package com.asistec.report.dto;

import com.asistec.attendance.entity.AttendanceStatus;

import java.time.LocalDate;

public record StudentHistoryResponse(
        Long studentId,
        String studentName,
        LocalDate attendanceDate,
        AttendanceStatus status
) {
}