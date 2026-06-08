package com.asistec.attendance.dto;

import java.util.List;

public record SaveAttendanceRequest(
        List<AttendanceStudentRequest> records
) {
}