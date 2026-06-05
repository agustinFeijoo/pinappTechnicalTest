import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, of } from 'rxjs';

import { API_BASE } from '../constants/api';
import {
  FALLBACK_SECTIONS,
  SectionListItem
} from '../models/section.model';

@Injectable({ providedIn: 'root' })
export class SectionService {

  private readonly url = `${API_BASE}/sections`;

  constructor(private http: HttpClient) {}

  getSections(): Observable<SectionListItem[]> {
    return this.http
      .get<SectionListItem[]>(this.url)
      .pipe(catchError(() => of(FALLBACK_SECTIONS)));
  }
}
