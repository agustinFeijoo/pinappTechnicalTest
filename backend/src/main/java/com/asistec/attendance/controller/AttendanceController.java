package com.asistec.attendance.controller;


import com.asistec.attendance.dto.SectionAttendanceResponse;
import com.asistec.attendance.service.AttendanceService;
import com.asistec.attendance.dto.SaveAttendanceRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sections")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("/{sectionId}/attendance/today")
    public SectionAttendanceResponse getTodayAttendance(
            @PathVariable Long sectionId
    ) {
        return attendanceService.getTodayAttendance(sectionId);
    }

    @PutMapping("/{sectionId}/attendance/today")
    public ResponseEntity<Void> saveAttendance(
            @PathVariable Long sectionId,
            @Valid @RequestBody SaveAttendanceRequest request
    ) {

        attendanceService.saveAttendance(
                sectionId,
                request
        );

        return ResponseEntity.ok().build();
    }
}