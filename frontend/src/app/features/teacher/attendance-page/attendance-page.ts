import {
  Component,
  OnInit,
  ChangeDetectorRef
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { finalize } from 'rxjs';

import {
  AttendanceStatus,
  SectionAttendanceResponse
} from '../../../core/models/attendance.model';

import { AttendanceService }
from '../../../core/services/attendance.service';

@Component({
  selector: 'app-attendance-page',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './attendance-page.html',
  styleUrl: './attendance-page.css'
})
export class AttendancePage implements OnInit {

  attendance: SectionAttendanceResponse | null = null;

  selectedSection = 1;

  hasChanges = false;

  loading = false;

  errorMessage = '';

  constructor(
    private attendanceService: AttendanceService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {

    console.log('AttendancePage initialized');

    this.loadAttendance();
  }

  loadAttendance(): void {

    console.log('Loading attendance...');

    this.loading = true;

    this.errorMessage = '';

    this.attendanceService
      .getTodayAttendance(this.selectedSection)
      .pipe(
        finalize(() => {

          this.loading = false;

          this.cdr.detectChanges();

          console.log(
            'Loading finished'
          );
        })
      )
      .subscribe({

        next: (data) => {

          console.log(
            'Attendance received',
            data
          );

          this.attendance = data;

          this.cdr.detectChanges();
        },

        error: (error) => {

          console.error(error);

          this.errorMessage =
            'Failed to load attendance';

          this.cdr.detectChanges();
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

    this.loading = true;

    const request = {
      students: this.attendance.students
        .filter(
          student =>
            student.status !== null
        )
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
      .pipe(
        finalize(() => {

          this.loading = false;

          this.cdr.detectChanges();
        })
      )
      .subscribe({

        next: () => {

          this.hasChanges = false;

          alert(
            'Attendance saved successfully'
          );

          this.loadAttendance();
        },

        error: (error) => {

          console.error(error);

          this.errorMessage =
            'Failed to save attendance';
        }
      });
  }
}