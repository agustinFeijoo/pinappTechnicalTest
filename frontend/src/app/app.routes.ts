import { Routes } from '@angular/router';

import { AttendancePageComponent }
from './features/teacher/attendance-page/attendance-page.component';
export const routes: Routes = [

  {
    path: '',
    redirectTo: 'teacher',
    pathMatch: 'full'
  },

  {
    path: 'teacher',
    component: AttendancePageComponent
  }
];