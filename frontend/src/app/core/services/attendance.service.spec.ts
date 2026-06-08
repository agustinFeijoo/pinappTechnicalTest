import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';

import { TestBed } from '@angular/core/testing';

import { AttendanceService }
from './attendance.service';

describe('AttendanceService', () => {

  let service: AttendanceService;
  let httpMock: HttpTestingController;

  beforeEach(() => {

    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });

    service =
      TestBed.inject(
        AttendanceService
      );

    httpMock =
      TestBed.inject(
        HttpTestingController
      );
  });

  afterEach(() => {

    httpMock.verify();
  });

  it('should request attendance', () => {

    service
      .getTodayAttendance(1)
      .subscribe();

    const req =
      httpMock.expectOne(
        req =>
          req.url.includes(
            '/sections/1/attendance/today'
          )
      );

    expect(req.request.method)
      .toBe('GET');

    req.flush({});
  });
});