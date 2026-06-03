package com.asistec.attendance.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SaveAttendanceRequest(

        @NotEmpty
        List<@Valid AttendanceStudentRequest> students

) {
}