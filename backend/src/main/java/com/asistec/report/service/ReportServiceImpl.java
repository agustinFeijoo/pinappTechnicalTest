package com.asistec.report.service;

import com.asistec.attendance.entity.AttendanceRecord;
import com.asistec.attendance.entity.AttendanceStatus;
import com.asistec.attendance.entity.Section;
import com.asistec.attendance.repository.AttendanceRecordRepository;
import com.asistec.attendance.repository.SectionRepository;
import com.asistec.attendance.repository.StudentRepository;
import com.asistec.report.dto.SectionSummaryResponse;
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
}