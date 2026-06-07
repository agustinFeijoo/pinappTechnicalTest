package com.asistec.attendance.repository;

import com.asistec.attendance.entity.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRecordRepository
        extends JpaRepository<AttendanceRecord, Long> {

    Optional<AttendanceRecord> findByStudentIdAndAttendanceDate(
            Long studentId,
            LocalDate attendanceDate
    );

    List<AttendanceRecord> findByAttendanceDate(
            LocalDate attendanceDate
    );

    List<AttendanceRecord> findByStudentIdAndAttendanceDateBetween(
            Long studentId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<AttendanceRecord>
    findByStudentSectionIdAndAttendanceDate(
            Long sectionId,
            LocalDate attendanceDate
    );

}