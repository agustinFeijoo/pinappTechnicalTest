package com.asistec.report.service;

import com.asistec.attendance.entity.AttendanceRecord;
import com.asistec.attendance.entity.AttendanceStatus;
import com.asistec.attendance.entity.Section;
import com.asistec.attendance.repository.AttendanceRecordRepository;
import com.asistec.attendance.repository.SectionRepository;
import com.asistec.attendance.repository.StudentRepository;
import com.asistec.report.dto.PendingSectionResponse;
import com.asistec.report.dto.SectionSummaryResponse;
import com.asistec.report.dto.StudentAttendanceSummaryResponse;
import com.asistec.report.dto.StudentHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl
        implements ReportService {

    private final SectionRepository sectionRepository;

    private final StudentRepository studentRepository;

    private final AttendanceRecordRepository
            attendanceRecordRepository;

    @Override
    public List<SectionSummaryResponse>
    getTodaySummary() {

        LocalDate today = LocalDate.now();

        return sectionRepository.findAll()
                .stream()
                .map(section -> buildSummary(
                        section,
                        today
                ))
                .toList();
    }

    private SectionSummaryResponse buildSummary(
            Section section,
            LocalDate today
    ) {

        List<AttendanceRecord> records =
                studentRepository
                        .findBySectionId(section.getId())
                        .stream()
                        .flatMap(student ->
                                attendanceRecordRepository
                                        .findByStudentIdAndAttendanceDate(
                                                student.getId(),
                                                today
                                        )
                                        .stream()
                        )
                        .toList();

        long presentCount =
                records.stream()
                        .filter(record ->
                                record.getStatus()
                                        == AttendanceStatus.PRESENT
                        )
                        .count();

        long absentCount =
                records.stream()
                        .filter(record ->
                                record.getStatus()
                                        == AttendanceStatus.ABSENT
                        )
                        .count();

        long lateCount =
                records.stream()
                        .filter(record ->
                                record.getStatus()
                                        == AttendanceStatus.LATE
                        )
                        .count();

        return new SectionSummaryResponse(
                section.getId(),
                section.getName(),
                presentCount,
                absentCount,
                lateCount
        );
    }

    @Override
    public List<PendingSectionResponse>
    getPendingSections() {

        LocalDate today = LocalDate.now();

        return sectionRepository.findAll()
                .stream()
                .filter(section ->
                        isPending(section, today)
                )
                .map(section ->
                        new PendingSectionResponse(
                                section.getId(),
                                section.getName()
                        )
                )
                .toList();
    }

    private boolean isPending(
            Section section,
            LocalDate today
    ) {

        return studentRepository
                .findBySectionId(section.getId())
                .stream()
                .flatMap(student ->
                        attendanceRecordRepository
                                .findByStudentIdAndAttendanceDate(
                                        student.getId(),
                                        today
                                )
                                .stream()
                )
                .findAny()
                .isEmpty();
    }

    @Override
    public List<StudentHistoryResponse> getStudentHistory(
            Long studentId,
            LocalDate startDate,
            LocalDate endDate
    ) {

        return attendanceRecordRepository
                .findByStudentIdAndAttendanceDateBetween(
                        studentId,
                        startDate,
                        endDate
                )
                .stream()
                .map(record ->
                        new StudentHistoryResponse(
                                record.getStudent().getId(),
                                record.getStudent().getFirstName()
                                        + " "
                                        + record.getStudent().getLastName(),
                                record.getAttendanceDate(),
                                record.getStatus()
                        )
                )
                .toList();
    }
    @Override
    public List<StudentAttendanceSummaryResponse>
    getStudentAttendanceSummary(
            LocalDate startDate,
            LocalDate endDate
    ) {

        return studentRepository.findAll()
                .stream()
                .map(student -> {

                    List<AttendanceRecord> records =
                            attendanceRecordRepository
                                    .findByStudentIdAndAttendanceDateBetween(
                                            student.getId(),
                                            startDate,
                                            endDate
                                    );

                    long present =
                            records.stream()
                                    .filter(r ->
                                            r.getStatus()
                                                    == AttendanceStatus.PRESENT
                                    )
                                    .count();

                    long late =
                            records.stream()
                                    .filter(r ->
                                            r.getStatus()
                                                    == AttendanceStatus.LATE
                                    )
                                    .count();

                    long absent =
                            records.stream()
                                    .filter(r ->
                                            r.getStatus()
                                                    == AttendanceStatus.ABSENT
                                    )
                                    .count();

                    return new StudentAttendanceSummaryResponse(
                            student.getId(),
                            student.getFirstName(),
                            student.getLastName(),
                            present,
                            late,
                            absent
                    );
                })
                .toList();
    }
}