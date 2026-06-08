import { environment } from '../../../environments/environment';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AttendanceSseService {

  connect(): Observable<string> {

    return new Observable(observer => {

      const eventSource =
        new EventSource(
          `${environment.apiUrl}/events/attendance`
        );

      eventSource.addEventListener(
        'attendance-updated',
        event => {

          observer.next(
            (event as MessageEvent).data
          );
        }
      );

      eventSource.onerror = error => {

        console.error(error);

        observer.error(error);

        eventSource.close();
      };

      return () => {

        eventSource.close();
      };
    });
  }
}