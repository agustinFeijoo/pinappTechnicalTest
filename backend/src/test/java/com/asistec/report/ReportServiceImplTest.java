package com.asistec.report.service;

import com.asistec.attendance.entity.*;
import com.asistec.attendance.repository.AttendanceRecordRepository;
import com.asistec.attendance.repository.SectionRepository;
import com.asistec.attendance.repository.StudentRepository;
import com.asistec.report.dto.PendingSectionResponse;
import com.asistec.report.dto.SectionSummaryResponse;
import com.asistec.report.dto.StudentAttendanceSummaryResponse;
import com.asistec.report.dto.StudentHistoryResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    private AttendanceRecordRepository attendanceRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SectionRepository sectionRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    @Test
    void getTodaySummary_shouldReturnSummaryPerSection() {

        Section section = Section.builder()
                .id(1L)
                .name("3A")
                .build();

        AttendanceRecord present =
                AttendanceRecord.builder()
                        .status(AttendanceStatus.PRESENT)
                        .build();

        AttendanceRecord absent =
                AttendanceRecord.builder()
                        .status(AttendanceStatus.ABSENT)
                        .build();

        when(sectionRepository.findAll())
                .thenReturn(List.of(section));

        when(attendanceRepository
                .findByStudentSectionIdAndAttendanceDate(
                        eq(1L),
                        any(LocalDate.class)
                ))
                .thenReturn(List.of(
                        present,
                        absent
                ));

        List<SectionSummaryResponse> result =
                reportService.getTodaySummary();

        assertEquals(1, result.size());

        assertEquals(
                1,
                result.get(0).presentCount()
        );

        assertEquals(
                1,
                result.get(0).absentCount()
        );
    }

    @Test
    void getPendingSections_shouldReturnSectionsWithoutAttendance() {

        Section section = Section.builder()
                .id(1L)
                .name("3A")
                .build();

        when(sectionRepository.findAll())
                .thenReturn(List.of(section));

        when(attendanceRepository
                .findByStudentSectionIdAndAttendanceDate(
                        eq(1L),
                        any(LocalDate.class)
                ))
                .thenReturn(List.of());

        List<PendingSectionResponse> result =
                reportService.getPendingSections();

        assertEquals(1, result.size());

        assertEquals(
                "3A",
                result.get(0).sectionName()
        );
    }

    @Test
    void getStudentHistory_shouldReturnRecords() {

        Student student =
                Student.builder()
                        .id(1L)
                        .firstName("Juan")
                        .lastName("Perez")
                        .build();

        AttendanceRecord record =
                AttendanceRecord.builder()
                        .student(student)
                        .attendanceDate(LocalDate.now())
                        .status(AttendanceStatus.PRESENT)
                        .build();

        when(attendanceRepository
                .findByStudentIdAndAttendanceDateBetween(
                        anyLong(),
                        any(),
                        any()
                ))
                .thenReturn(List.of(record));

        List<StudentHistoryResponse> result =
                reportService.getStudentHistory(
                        1L,
                        LocalDate.now().minusDays(1),
                        LocalDate.now()
                );

        assertEquals(1, result.size());

        assertEquals(
                AttendanceStatus.PRESENT,
                result.get(0).status()
        );
    }

    @Test
    void getStudentsSummary_shouldCalculateTotals() {

        Student student =
                Student.builder()
                        .id(1L)
                        .firstName("Juan")
                        .lastName("Perez")
                        .build();

        AttendanceRecord present =
                AttendanceRecord.builder()
                        .status(AttendanceStatus.PRESENT)
                        .build();

        AttendanceRecord late =
                AttendanceRecord.builder()
                        .status(AttendanceStatus.LATE)
                        .build();

        when(studentRepository.findAll())
                .thenReturn(List.of(student));

        when(attendanceRepository
                .findByStudentIdAndAttendanceDateBetween(
                        anyLong(),
                        any(),
                        any()
                ))
                .thenReturn(List.of(
                        present,
                        late
                ));

        List<StudentAttendanceSummaryResponse> result =
                reportService.getStudentsSummary(
                        LocalDate.now().minusDays(30),
                        LocalDate.now()
                );

        assertEquals(1, result.size());

        assertEquals(
                1,
                result.get(0).presentCount()
        );

        assertEquals(
                1,
                result.get(0).lateCount()
        );

        assertEquals(
                0,
                result.get(0).absentCount()
        );
    }
}