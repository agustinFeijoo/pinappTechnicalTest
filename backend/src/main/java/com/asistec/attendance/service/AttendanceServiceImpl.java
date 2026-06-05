package com.asistec.attendance.service;

import com.asistec.attendance.dto.*;
import com.asistec.attendance.entity.AttendanceRecord;
import com.asistec.attendance.entity.Section;
import com.asistec.attendance.entity.Student;
import com.asistec.attendance.repository.AttendanceRecordRepository;
import com.asistec.attendance.repository.SectionRepository;
import com.asistec.attendance.repository.StudentRepository;
import com.asistec.common.exception.BusinessRuleException;
import com.asistec.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final StudentRepository studentRepository;
    private final SectionRepository sectionRepository;
    private final AttendanceRecordRepository attendanceRepository;

    private final AttendanceEventService attendanceEventService;

    @Override
    @Transactional(readOnly = true)
    public SectionAttendanceResponse getTodayAttendance(
            Long sectionId
    ) {

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Section not found"
                        ));

        LocalDate today = LocalDate.now();

        List<StudentAttendanceResponse> students =
                studentRepository.findBySectionId(sectionId)
                        .stream()
                        .map(student -> {

                            AttendanceRecord record =
                                    attendanceRepository
                                            .findByStudentIdAndAttendanceDate(
                                                    student.getId(),
                                                    today
                                            )
                                            .orElse(null);

                            return new StudentAttendanceResponse(
                                    student.getId(),
                                    student.getFirstName()
                                            + " "
                                            + student.getLastName(),
                                    record != null
                                            ? record.getStatus()
                                            : null
                            );
                        })
                        .toList();

        return new SectionAttendanceResponse(
                section.getId(),
                section.getName(),
                today,
                students
        );
    }

    @Override
    public void saveAttendance(
            Long sectionId,
            SaveAttendanceRequest request
    ) {

        LocalDate today = LocalDate.now();

        sectionRepository.findById(sectionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Section not found"
                        ));

        for (AttendanceStudentRequest item : request.students()) {

            Student student = studentRepository.findById(
                    item.studentId()
            ).orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Student not found"
                    ));

            if (!student.getSection()
                    .getId()
                    .equals(sectionId)) {

                throw new BusinessRuleException(
                        "Student does not belong to section"
                );
            }

            AttendanceRecord record =
                    attendanceRepository
                            .findByStudentIdAndAttendanceDate(
                                    student.getId(),
                                    today
                            )
                            .orElse(
                                    AttendanceRecord.builder()
                                            .student(student)
                                            .attendanceDate(today)
                                            .build()
                            );

            record.setStatus(item.status());
            record.setUpdatedAt(LocalDateTime.now());

            attendanceRepository.save(record);
        }

        attendanceEventService.publishAttendanceUpdated();
    }
}