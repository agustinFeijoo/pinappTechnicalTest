import { Routes } from '@angular/router';

import { RoleSelectComponent } from './features/home/role-select.component';

import { AttendancePage }
from './features/teacher/attendance-page/attendance-page';

import { DashboardPage }
from './features/coordinator/dashboard-page/dashboard-page';

import { StudentHistoryPage }
from './features/coordinator/student-history-page/student-history-page';

export const routes: Routes = [
  {
    path: '',
    component: RoleSelectComponent
  },
  {
    path: 'teacher',
    component: AttendancePage
  },
  {
    path: 'coordinator',
    component: DashboardPage
  },
  {
    path: 'coordinator/history',
    component: StudentHistoryPage
  },
  {
    path: '**',
    redirectTo: ''
  }
];