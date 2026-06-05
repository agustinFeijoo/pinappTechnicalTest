package com.asistec.attendance.service;


import com.asistec.attendance.dto.SaveAttendanceRequest;
import com.asistec.attendance.dto.SectionAttendanceResponse;

public interface AttendanceService {

    SectionAttendanceResponse getTodayAttendance(Long sectionId);

    void saveAttendance(
            Long sectionId,
            SaveAttendanceRequest request
    );
}