import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Valuation } from '../models/valuation.model';

@Injectable({
  providedIn: 'root'
})
export class ValuationService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/valuations`;

  createValuation(valuation: Valuation): Observable<Valuation> {
    return this.http.post<Valuation>(this.apiUrl, valuation);
  }

  getAllValuations(): Observable<Valuation[]> {
    return this.http.get<Valuation[]>(this.apiUrl);
  }

  getValuationById(id: number): Observable<Valuation> {
    return this.http.get<Valuation>(`${this.apiUrl}/${id}`);
  }

  updateValuation(id: number, valuation: Valuation): Observable<Valuation> {
    return this.http.put<Valuation>(`${this.apiUrl}/${id}`, valuation);
  }

  deleteValuationById(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getByClient(clientId: number): Observable<Valuation[]> {
    return this.http.get<Valuation[]>(`${this.apiUrl}/client/${clientId}`);
  }

  getByShip(shipId: number): Observable<Valuation[]> {
    return this.http.get<Valuation[]>(`${this.apiUrl}/ship/${shipId}`);
  }

  getByPort(portId: number): Observable<Valuation[]> {
    return this.http.get<Valuation[]>(`${this.apiUrl}/port/${portId}`);
  }

  getByTrip(tripId: number): Observable<Valuation[]> {
    return this.http.get<Valuation[]>(`${this.apiUrl}/trip/${tripId}`);
  }

  getByContainer(containerId: number): Observable<Valuation[]> {
    return this.http.get<Valuation[]>(`${this.apiUrl}/container/${containerId}`);
  }

  getByGroundTransport(transportId: number): Observable<Valuation[]> {
    return this.http.get<Valuation[]>(`${this.apiUrl}/ground-transport/${transportId}`);
  }

  exportExcel(): void {
    window.open(`${this.apiUrl}/export/excel`, '_blank');
  }

  exportExcelByClientAndDate(clientId: number, startDate: string, endDate: string): void {
    const params = new HttpParams()
      .set('clientId', clientId.toString())
      .set('startDate', startDate)
      .set('endDate', endDate);

    window.open(`${this.apiUrl}/export/excel/client?${params.toString()}`, '_blank');
  }
}
