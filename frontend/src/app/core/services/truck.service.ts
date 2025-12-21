import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Truck } from '../models/truck.model';
import { ApiResponse } from '../models/api-response.model';

@Injectable({
  providedIn: 'root'
})
export class TruckService {
  private http = inject(HttpClient);
  public apiUrl = `${environment.apiUrl}/camiones`;

  getAllTrucks(): Observable<ApiResponse<Truck[]>> {
    return this.http.get<ApiResponse<Truck[]>>(this.apiUrl);
  }

  getTruckById(id: number): Observable<ApiResponse<Truck>> {
    return this.http.get<ApiResponse<Truck>>(`${this.apiUrl}/${id}`);
  }

  createTruck(truck: Truck): Observable<ApiResponse<Truck>> {
    return this.http.post<ApiResponse<Truck>>(`${this.apiUrl}/guardar`, truck);
  }

  updateTruck(id: number, truck: Truck): Observable<ApiResponse<Truck>> {
    return this.http.put<ApiResponse<Truck>>(`${this.apiUrl}/modificar/${id}`, truck);
  }

  deleteTruck(id: number): Observable<ApiResponse<any>> {
    return this.http.delete<ApiResponse<any>>(`${this.apiUrl}/eliminar/${id}`);
  }
}
