package com.asistec.attendance.dto;

import java.time.LocalDate;
import java.util.List;

public record SectionAttendanceResponse(
        Long sectionId,
        String sectionName,
        LocalDate date,
        List<StudentAttendanceResponse> students
) {
}