import {
  Component,
  OnInit,
  ChangeDetectorRef
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { finalize } from 'rxjs';

import {
  AttendanceStatus,
  SectionAttendanceResponse
} from '../../../core/models/attendance.model';

import { AttendanceService }
from '../../../core/services/attendance.service';

import { SectionService }
from '../../../core/services/section.service';

@Component({
  selector: 'app-attendance-page',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink
  ],
  templateUrl: './attendance-page.html',
  styleUrl: './attendance-page.css'
})
export class AttendancePage implements OnInit {

  attendance: SectionAttendanceResponse | null = null;

  selectedSection = 1;

  sections = [
    { id: 1, name: '3A' },
    { id: 2, name: '3B' },
    { id: 3, name: '4A' },
    { id: 4, name: '4B' }
  ];

  hasChanges = false;
  loading = false;
  errorMessage = '';

  constructor(
    private attendanceService: AttendanceService,
    private sectionService: SectionService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {

    this.sectionService
      .getSections()
      .subscribe({

        next: sections => {

          this.sections = sections;

          if (sections.length > 0) {

            this.selectedSection =
              sections[0].id;

            this.loadAttendance();
          }

          this.cdr.detectChanges();
        },

        error: error => {

          console.error(error);

          this.errorMessage =
            'Failed to load sections';

          this.cdr.detectChanges();
        }
      });
  }

  loadAttendance(): void {
    this.loading = true;
    this.errorMessage = '';

    this.attendanceService
      .getTodayAttendance(this.selectedSection)
      .pipe(
        finalize(() => {

          this.loading = false;

          this.cdr.detectChanges();
        })
      )
      .subscribe({

        next: data => {
          this.attendance = data;

          this.cdr.detectChanges();
        },

        error: error => {

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

  onSectionChange(): void {

    this.hasChanges = false;
    this.attendance = null;

    this.loadAttendance();
  }

  save(): void {

    if (!this.attendance) {
      return;
    }

    this.loading = true;
    this.errorMessage = '';

const request = {
  records: this.attendance.students
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

        error: error => {

          console.error(error);

          this.errorMessage =
            'Failed to save attendance';

          this.cdr.detectChanges();
        }
      });
  }
}