import {
  Component,
  OnInit,
  OnDestroy,
  ChangeDetectorRef
} from '@angular/core';

import { CommonModule } from '@angular/common';

import {
  interval,
  Subscription
} from 'rxjs';

import { ReportService }
from '../../../core/services/report.service';

import {
  PendingSection,
  SectionSummary
} from '../../../core/models/report.model';

@Component({
  selector: 'app-dashboard-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard-page.html',
  styleUrl: './dashboard-page.css'
})
export class DashboardPage
  implements OnInit, OnDestroy {

  summaries: SectionSummary[] = [];

  pendingSections: PendingSection[] = [];

  loading = true;

  errorMessage = '';

  private refreshSubscription?: Subscription;

  constructor(
    private reportService: ReportService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {

    this.refreshData();

    this.refreshSubscription =
      interval(5000)
        .subscribe(() => {

          this.refreshData();

        });
  }

  ngOnDestroy(): void {

    this.refreshSubscription?.unsubscribe();
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