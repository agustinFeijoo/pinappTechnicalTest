package com.asistec.attendance.dto;

import com.asistec.attendance.entity.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

public record AttendanceStudentRequest(

        @NotNull
        Long studentId,

        @NotNull
        AttendanceStatus status

) {
}