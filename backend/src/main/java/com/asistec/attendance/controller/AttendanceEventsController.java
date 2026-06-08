package com.asistec.attendance.controller;

import com.asistec.attendance.service.AttendanceEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class AttendanceEventsController {

    private final AttendanceEventService attendanceEventService;

    @GetMapping("/attendance")
    public SseEmitter streamAttendance() throws IOException {
        return attendanceEventService.subscribe();
    }
}