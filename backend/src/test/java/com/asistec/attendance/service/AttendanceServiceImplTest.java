package com.asistec.attendance.service;

import com.asistec.attendance.dto.AttendanceStudentRequest;
import com.asistec.attendance.dto.SaveAttendanceRequest;
import com.asistec.attendance.entity.*;
import com.asistec.attendance.repository.AttendanceRecordRepository;
import com.asistec.attendance.repository.SectionRepository;
import com.asistec.attendance.repository.StudentRepository;
import com.asistec.common.exception.BusinessRuleException;
import com.asistec.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private AttendanceRecordRepository attendanceRepository;

    @Mock
    private AttendanceEventService attendanceEventService;

    @InjectMocks
    private AttendanceServiceImpl attendanceService;

    @Test
    void saveAttendance_shouldPersistRecords() {

        Section section = Section.builder()
                .id(1L)
                .name("3A")
                .build();

        Student student = Student.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Perez")
                .section(section)
                .build();

        SaveAttendanceRequest request =
                new SaveAttendanceRequest(
                        List.of(
                                new AttendanceStudentRequest(
                                        1L,
                                        AttendanceStatus.PRESENT
                                )
                        )
                );

        when(sectionRepository.findById(1L))
                .thenReturn(Optional.of(section));

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(attendanceRepository
                .findByStudentIdAndAttendanceDate(
                        eq(1L),
                        any(LocalDate.class)
                ))
                .thenReturn(Optional.empty());

        attendanceService.saveAttendance(
                1L,
                request
        );

        verify(attendanceRepository)
                .save(any(AttendanceRecord.class));

        verify(attendanceEventService)
                .publishAttendanceUpdated();
    }

    @Test
    void saveAttendance_shouldThrowWhenSectionNotFound() {

        when(sectionRepository.findById(1L))
                .thenReturn(Optional.empty());

        SaveAttendanceRequest request =
                new SaveAttendanceRequest(List.of());

        assertThrows(
                ResourceNotFoundException.class,
                () -> attendanceService.saveAttendance(
                        1L,
                        request
                )
        );
    }

    @Test
    void saveAttendance_shouldThrowWhenStudentNotInSection() {

        Section section1 =
                Section.builder()
                        .id(1L)
                        .build();

        Section section2 =
                Section.builder()
                        .id(2L)
                        .build();

        Student student =
                Student.builder()
                        .id(1L)
                        .section(section2)
                        .build();

        when(sectionRepository.findById(1L))
                .thenReturn(Optional.of(section1));

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        SaveAttendanceRequest request =
                new SaveAttendanceRequest(
                        List.of(
                                new AttendanceStudentRequest(
                                        1L,
                                        AttendanceStatus.PRESENT
                                )
                        )
                );

        assertThrows(
                BusinessRuleException.class,
                () -> attendanceService.saveAttendance(
                        1L,
                        request
                )
        );
    }
}