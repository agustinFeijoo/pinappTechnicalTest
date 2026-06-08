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

    public SseEmitter subscribe() throws IOException {

        SseEmitter emitter =
                new SseEmitter(30 * 60 * 1000L);

        emitter.send(
                SseEmitter.event()
                        .name("heartbeat")
                        .data("ping")
        );

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

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(
                        SseEmitter.event()
                                .name("attendance-updated")
                                .data("updated")
                );

            } catch (IOException | IllegalStateException ex) {
                try {
                    emitter.complete();
                } catch (Exception ignored) {}

                emitters.remove(emitter);
            }
        }
    }
}