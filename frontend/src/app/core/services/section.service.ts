import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { API_BASE }
from '../constants/api';

import { Section }
from '../models/section.model';

@Injectable({
  providedIn: 'root'
})
export class SectionService {

  private readonly url =
    `${API_BASE}/sections`;

  constructor(
    private http: HttpClient
  ) {}

  getSections() {

    return this.http.get<Section[]>(
      this.url
    );
  }
}