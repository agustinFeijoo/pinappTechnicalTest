import {
  Component,
  OnInit,
  OnDestroy,
  ChangeDetectorRef
} from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';

import { ReportService }
from '../../../core/services/report.service';

import { AttendanceSseService }
from '../../../core/services/attendanceSSE.service';

import {
  PendingSection,
  SectionSummary
} from '../../../core/models/report.model';

@Component({
  selector: 'app-dashboard-page',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    FormsModule
  ],
  templateUrl: './dashboard-page.html',
  styleUrl: './dashboard-page.css'
})
export class DashboardPage
  implements OnInit, OnDestroy {

  summaries: SectionSummary[] = [];
  pendingSections: PendingSection[] = [];

  loading = true;
  errorMessage = '';

  private sseSubscription?: Subscription;

  constructor(
    private reportService: ReportService,
    private attendanceSseService: AttendanceSseService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {

    this.refreshData();

    this.sseSubscription =
      this.attendanceSseService
        .connect()
        .subscribe({

          next: () => {
            this.refreshData();
          },

          error: error => {

            console.error(
              'SSE connection error',
              error
            );
          }
        });
  }

  ngOnDestroy(): void {

    this.sseSubscription?.unsubscribe();
  }

  private refreshData(): void {

    this.loading = true;
    this.errorMessage = '';

    this.loadSummary();
    this.loadPendingSections();
  }

  loadSummary(): void {

    this.reportService
      .getTodaySummary()
      .subscribe({

        next: data => {

          this.summaries = data;

          this.cdr.detectChanges();
        },

        error: err => {

          console.error(err);

          this.errorMessage =
            'Failed to load attendance summary';

          this.cdr.detectChanges();
        }
      });
  }

  loadPendingSections(): void {

    this.reportService
      .getPendingSections()
      .subscribe({

        next: data => {

          this.pendingSections = data;

          this.loading = false;

          this.cdr.detectChanges();
        },

        error: err => {

          console.error(err);

          this.errorMessage =
            'Failed to load pending sections';

          this.loading = false;

          this.cdr.detectChanges();
        }
      });
  }
}