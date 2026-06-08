package com.asistec.attendance.service;


import com.asistec.attendance.dto.SectionAttendanceResponse;
import com.asistec.attendance.dto.SaveAttendanceRequest;

public interface AttendanceService {

    SectionAttendanceResponse getTodayAttendance(Long sectionId);

    void saveAttendance(
            Long sectionId,
            SaveAttendanceRequest request
    );
}