package com.asistec.attendance.controller;

import com.asistec.attendance.service.AttendanceEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class AttendanceStreamController {

    private final AttendanceEventService
            attendanceEventService;

    @GetMapping("/stream")
    public SseEmitter stream() {

        return attendanceEventService.subscribe();
    }
}