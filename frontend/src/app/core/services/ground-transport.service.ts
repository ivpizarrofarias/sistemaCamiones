import {inject} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";
import {GroundTransport} from "../models/entities.model";

export class GroundTransportService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/ground-transports`;

  getAllGroundTransports(): Observable<GroundTransport[]> {
    return this.http.get<GroundTransport[]>(this.apiUrl);
  }

  getGroundTransportById(id: number): Observable<GroundTransport> {
    return this.http.get<GroundTransport>(`${this.apiUrl}/${id}`);
  }

  createGroundTransport(transport: GroundTransport): Observable<GroundTransport> {
    return this.http.post<GroundTransport>(this.apiUrl, transport);
  }

  updateGroundTransport(id: number, transport: GroundTransport): Observable<GroundTransport> {
    return this.http.put<GroundTransport>(`${this.apiUrl}/${id}`, transport);
  }

  deleteGroundTransport(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
