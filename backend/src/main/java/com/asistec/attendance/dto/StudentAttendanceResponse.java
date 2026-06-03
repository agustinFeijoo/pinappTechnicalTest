package com.asistec.attendance.dto;

import com.asistec.attendance.entity.AttendanceStatus;

public record StudentAttendanceResponse(
        Long studentId,
        String fullName,
        AttendanceStatus status
) {
}