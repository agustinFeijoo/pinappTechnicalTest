import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import {
  AttendanceStatus,
  SectionAttendanceResponse
} from '../../../core/models/attendance.model';

import { AttendanceService } from '../../../core/services/attendance.service';

@Component({
  selector: 'app-attendance-page',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './attendance-page.component.html'
})
export class AttendancePageComponent implements OnInit {

  attendance: SectionAttendanceResponse | null = null;

  selectedSection = 1;

  hasChanges = false;

  loading = false;

  errorMessage = '';

  constructor(
    private attendanceService: AttendanceService
  ) {}

  ngOnInit(): void {

    console.log('AttendancePageComponent initialized');

    this.loadAttendance();
  }

  loadAttendance(): void {

    console.log(
      'Loading attendance for section:',
      this.selectedSection
    );

    this.loading = true;
    this.errorMessage = '';

    this.attendanceService
      .getTodayAttendance(this.selectedSection)
      .subscribe({
        next: (data) => {

          console.log(
            'Attendance loaded successfully:',
            data
          );

          this.attendance = data;
          this.loading = false;
        },

        error: (error) => {

          console.error(
            'Error loading attendance:',
            error
          );

          this.errorMessage =
            'Failed to load attendance';

          this.loading = false;
        }
      });
  }

  updateStatus(
    index: number,
    status: AttendanceStatus
  ): void {

    if (!this.attendance) {
      return;
    }

    this.attendance.students[index].status =
      status;

    this.hasChanges = true;
  }

  save(): void {

    if (!this.attendance) {
      return;
    }

    const request = {
      students: this.attendance.students
        .filter(student => student.status !== null)
        .map(student => ({
          studentId: student.studentId,
          status: student.status!
        }))
    };

    this.attendanceService
      .saveAttendance(
        this.selectedSection,
        request
      )
      .subscribe({
        next: () => {

          this.hasChanges = false;

          alert('Attendance saved successfully');

          this.loadAttendance();
        },

        error: (error) => {

          console.error(
            'Error saving attendance:',
            error
          );

          alert('Failed to save attendance');
        }
      });
  }
}