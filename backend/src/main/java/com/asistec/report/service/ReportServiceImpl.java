package com.asistec.report.service;

import com.asistec.attendance.entity.AttendanceRecord;
import com.asistec.attendance.entity.AttendanceStatus;
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
public class ReportServiceImpl implements ReportService {

    private final AttendanceRecordRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final SectionRepository sectionRepository;

    @Override
    public List<SectionSummaryResponse> getTodaySummary() {

        LocalDate today = LocalDate.now();

        return sectionRepository.findAll()
                .stream()
                .map(section -> {

                    List<AttendanceRecord> records =
                            attendanceRepository
                                    .findByStudentSectionIdAndAttendanceDate(
                                            section.getId(),
                                            today
                                    );

                    long presentCount =
                            records.stream()
                                    .filter(r ->
                                            r.getStatus() == AttendanceStatus.PRESENT)
                                    .count();

                    long lateCount =
                            records.stream()
                                    .filter(r ->
                                            r.getStatus() == AttendanceStatus.LATE)
                                    .count();

                    long absentCount =
                            records.stream()
                                    .filter(r ->
                                            r.getStatus() == AttendanceStatus.ABSENT)
                                    .count();

                    return new SectionSummaryResponse(
                            section.getId(),
                            section.getName(),
                            presentCount,
                            absentCount,
                            lateCount
                    );
                })
                .toList();
    }
    @Override
    public List<PendingSectionResponse> getPendingSections() {

        LocalDate today = LocalDate.now();

        return sectionRepository.findAll()
                .stream()
                .filter(section -> {

                    List<AttendanceRecord> records =
                            attendanceRepository
                                    .findByStudentSectionIdAndAttendanceDate(
                                            section.getId(),
                                            today
                                    );

                    return records.isEmpty();
                })
                .map(section ->
                        new PendingSectionResponse(
                                section.getId(),
                                section.getName()
                        )
                )
                .toList();
    }

    @Override
    public List<StudentHistoryResponse> getStudentHistory(
            Long studentId,
            LocalDate startDate,
            LocalDate endDate
    ) {

        return attendanceRepository
                .findByStudentIdAndAttendanceDateBetween(
                        studentId,
                        startDate,
                        endDate
                )
                .stream()
                .map(record -> new StudentHistoryResponse(
                        record.getStudent().getId(),
                        record.getStudent().getFirstName()
                                + " "
                                + record.getStudent().getLastName(),
                        record.getAttendanceDate(),
                        record.getStatus()
                ))
                .toList();
    }

    @Override
    public List<StudentAttendanceSummaryResponse> getStudentsSummary(
            LocalDate startDate,
            LocalDate endDate
    ) {

        return studentRepository.findAll()
                .stream()
                .map(student -> {

                    List<AttendanceRecord> records =
                            attendanceRepository
                                    .findByStudentIdAndAttendanceDateBetween(
                                            student.getId(),
                                            startDate,
                                            endDate
                                    );

                    long presentCount =
                            records.stream()
                                    .filter(r ->
                                            r.getStatus() == AttendanceStatus.PRESENT)
                                    .count();

                    long lateCount =
                            records.stream()
                                    .filter(r ->
                                            r.getStatus() == AttendanceStatus.LATE)
                                    .count();

                    long absentCount =
                            records.stream()
                                    .filter(r ->
                                            r.getStatus() == AttendanceStatus.ABSENT)
                                    .count();

                    return new StudentAttendanceSummaryResponse(
                            student.getId(),
                            student.getFirstName(),
                            student.getLastName(),
                            presentCount,
                            lateCount,
                            absentCount
                    );
                })
                .toList();
    }
}