import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';

import { TestBed } from '@angular/core/testing';
import { ReportService }
from './report.service';

describe('ReportService', () => {

  let service: ReportService;
  let httpMock: HttpTestingController;

  beforeEach(() => {

    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });

    service =
      TestBed.inject(ReportService);

    httpMock =
      TestBed.inject(
        HttpTestingController
      );
  });

  afterEach(() => {

    httpMock.verify();
  });

  it('should call summary endpoint', () => {

    service
      .getTodaySummary()
      .subscribe();

    const req =
      httpMock.expectOne(
        req =>
          req.url.includes(
            '/reports/summary/today'
          )
      );

    expect(req.request.method)
      .toBe('GET');

    req.flush([]);
  });
});