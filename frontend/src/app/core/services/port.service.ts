import {inject} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";
import {Port} from "../models/entities.model";

export class PortService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/ports`;

  getAllPorts(): Observable<Port[]> {
    return this.http.get<Port[]>(this.apiUrl);
  }

  getPortById(id: number): Observable<Port> {
    return this.http.get<Port>(`${this.apiUrl}/${id}`);
  }

  createPort(port: Port): Observable<Port> {
    return this.http.post<Port>(this.apiUrl, port);
  }

  updatePort(id: number, port: Port): Observable<Port> {
    return this.http.put<Port>(`${this.apiUrl}/${id}`, port);
  }

  deletePort(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
