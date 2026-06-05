package com.asistec.attendance.service;

import com.asistec.attendance.dto.AttendanceStudentRequest;
import com.asistec.attendance.dto.SaveAttendanceRequest;
import com.asistec.attendance.dto.SectionAttendanceResponse;
import com.asistec.attendance.entity.AttendanceRecord;
import com.asistec.attendance.entity.AttendanceStatus;
import com.asistec.attendance.entity.Section;
import com.asistec.attendance.entity.Student;
import com.asistec.attendance.repository.AttendanceRecordRepository;
import com.asistec.attendance.repository.SectionRepository;
import com.asistec.attendance.repository.StudentRepository;
import com.asistec.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void shouldSaveAttendanceSuccessfully() {

        Section section = Section.builder()
                .id(1L)
                .name("3A")
                .build();

        Student student = Student.builder()
                .id(1L)
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
                        anyLong(),
                        any()
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
    void shouldThrowWhenStudentBelongsToDifferentSection() {

        Section section1 = Section.builder()
                .id(1L)
                .build();

        Section section2 = Section.builder()
                .id(2L)
                .build();

        Student student = Student.builder()
                .id(1L)
                .section(section2)
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
                .thenReturn(Optional.of(section1));

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        assertThrows(
                RuntimeException.class,
                () -> attendanceService.saveAttendance(
                        1L,
                        request
                )
        );
    }
    @Test
    void shouldReturnTodayAttendance() {

        Section section = Section.builder()
                .id(1L)
                .name("3A")
                .build();

        Student student = Student.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .section(section)
                .build();

        when(sectionRepository.findById(1L))
                .thenReturn(Optional.of(section));

        when(studentRepository.findBySectionId(1L))
                .thenReturn(List.of(student));

        when(attendanceRepository
                .findByStudentIdAndAttendanceDate(
                        anyLong(),
                        any()
                ))
                .thenReturn(Optional.empty());

        SectionAttendanceResponse response =
                attendanceService.getTodayAttendance(1L);

        assertEquals(1L, response.sectionId());
        assertEquals("3A", response.sectionName());
        assertEquals(1, response.students().size());
    }
    @Test
    void shouldThrowWhenSectionDoesNotExist() {

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
    void shouldThrowWhenStudentDoesNotExist() {

        Section section = Section.builder()
                .id(1L)
                .build();

        when(sectionRepository.findById(1L))
                .thenReturn(Optional.of(section));

        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());

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
                ResourceNotFoundException.class,
                () -> attendanceService.saveAttendance(
                        1L,
                        request
                )
        );
    }
    @Test
    void shouldUpdateExistingAttendanceRecord() {

        Section section = Section.builder()
                .id(1L)
                .build();

        Student student = Student.builder()
                .id(1L)
                .section(section)
                .build();

        AttendanceRecord existing =
                AttendanceRecord.builder()
                        .student(student)
                        .attendanceDate(LocalDate.now())
                        .status(AttendanceStatus.ABSENT)
                        .build();

        when(sectionRepository.findById(1L))
                .thenReturn(Optional.of(section));

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(attendanceRepository
                .findByStudentIdAndAttendanceDate(
                        anyLong(),
                        any()
                ))
                .thenReturn(Optional.of(existing));

        SaveAttendanceRequest request =
                new SaveAttendanceRequest(
                        List.of(
                                new AttendanceStudentRequest(
                                        1L,
                                        AttendanceStatus.PRESENT
                                )
                        )
                );

        attendanceService.saveAttendance(
                1L,
                request
        );

        assertEquals(
                AttendanceStatus.PRESENT,
                existing.getStatus()
        );

        verify(attendanceRepository)
                .save(existing);
    }
}