import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { API_BASE } from '../constants/api';

@Injectable({ providedIn: 'root' })
export class SseService {

  connectAttendanceUpdates(): Observable<unknown> {
    return new Observable(observer => {
      const eventSource = new EventSource(
        `${API_BASE}/events/attendance`
      );

      const handleEvent = (event: MessageEvent) => {
        try {
          observer.next(JSON.parse(event.data));
        } catch {
          observer.next(event.data);
        }
      };

      eventSource.addEventListener(
        'attendance-updated',
        handleEvent
      );
      eventSource.onmessage = handleEvent;

      eventSource.onerror = () => {
        eventSource.close();
      };

      return () => eventSource.close();
    });
  }
}
