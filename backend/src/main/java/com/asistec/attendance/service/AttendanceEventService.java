package com.asistec.attendance.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class AttendanceEventService {

    private final List<SseEmitter> emitters =
            new CopyOnWriteArrayList<>();

    public SseEmitter subscribe() {

        SseEmitter emitter =
                new SseEmitter(Long.MAX_VALUE);

        emitters.add(emitter);

        emitter.onCompletion(
                () -> emitters.remove(emitter));

        emitter.onTimeout(
                () -> emitters.remove(emitter));

        emitter.onError(
                error -> emitters.remove(emitter));

        return emitter;
    }

    public void publishAttendanceUpdated() {

        emitters.forEach(emitter -> {

            try {

                emitter.send(
                        SseEmitter.event()
                                .name("attendance-updated")
                                .data("updated")
                );

            } catch (IOException ex) {

                emitter.complete();

            }

        });
    }
}