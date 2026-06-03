package com.asistec.common.config;

import com.asistec.attendance.entity.*;
import com.asistec.attendance.repository.AttendanceRecordRepository;
import com.asistec.attendance.repository.GradeRepository;
import com.asistec.attendance.repository.SectionRepository;
import com.asistec.attendance.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final GradeRepository gradeRepository;
    private final SectionRepository sectionRepository;
    private final StudentRepository studentRepository;
    private final AttendanceRecordRepository attendanceRepository;

    @Bean
    CommandLineRunner loadData() {

        return args -> {

            if (gradeRepository.count() > 0) {
                return;
            }

            // Grades
            Grade thirdGrade = gradeRepository.save(
                    Grade.builder()
                            .name("3rd Grade")
                            .build()
            );

            Grade fourthGrade = gradeRepository.save(
                    Grade.builder()
                            .name("4th Grade")
                            .build()
            );

            // Sections
            Section section3A = sectionRepository.save(
                    Section.builder()
                            .name("3A")
                            .grade(thirdGrade)
                            .build()
            );

            Section section3B = sectionRepository.save(
                    Section.builder()
                            .name("3B")
                            .grade(thirdGrade)
                            .build()
            );

            Section section4A = sectionRepository.save(
                    Section.builder()
                            .name("4A")
                            .grade(fourthGrade)
                            .build()
            );

            Section section4B = sectionRepository.save(
                    Section.builder()
                            .name("4B")
                            .grade(fourthGrade)
                            .build()
            );

            // Students
            List<Student> students3A = createStudents(section3A,
                    "Juan", "Pedro", "Luis", "Ana", "María", "Lucía");

            List<Student> students3B = createStudents(section3B,
                    "Carlos", "Jorge", "Sofía", "Valentina", "Camila", "Elena");

            List<Student> students4A = createStudents(section4A,
                    "Martín", "Tomás", "Mateo", "Julieta", "Emma", "Mora");

            List<Student> students4B = createStudents(section4B,
                    "Agustín", "Thiago", "Benjamín", "Olivia", "Isabella", "Martina");

            studentRepository.saveAll(students3A);
            studentRepository.saveAll(students3B);
            studentRepository.saveAll(students4A);
            studentRepository.saveAll(students4B);

            // Historical attendance for last 5 business days
            seedAttendanceForLastBusinessDays(students3A);
            seedAttendanceForLastBusinessDays(students4A);

            // IMPORTANT:
            // 3B and 4B will have NO attendance today
            // so coordinator can see pending sections.
        };
    }

    private List<Student> createStudents(
            Section section,
            String... names
    ) {

        return List.of(names)
                .stream()
                .map(name ->
                        Student.builder()
                                .firstName(name)
                                .lastName("Student")
                                .section(section)
                                .build())
                .toList();
    }

    private void seedAttendanceForLastBusinessDays(
            List<Student> students
    ) {

        LocalDate date = LocalDate.now();

        int createdDays = 0;

        while (createdDays < 5) {

            if (date.getDayOfWeek() != DayOfWeek.SATURDAY
                    && date.getDayOfWeek() != DayOfWeek.SUNDAY) {

                for (Student student : students) {

                    AttendanceStatus status;

                    int mod = (int) (student.getId() % 3);

                    if (mod == 0) {
                        status = AttendanceStatus.PRESENT;
                    } else if (mod == 1) {
                        status = AttendanceStatus.ABSENT;
                    } else {
                        status = AttendanceStatus.LATE;
                    }

                    AttendanceRecord record =
                            AttendanceRecord.builder()
                                    .student(student)
                                    .attendanceDate(date)
                                    .status(status)
                                    .updatedAt(LocalDateTime.now())
                                    .build();

                    attendanceRepository.save(record);
                }

                createdDays++;
            }

            date = date.minusDays(1);
        }
    }
}