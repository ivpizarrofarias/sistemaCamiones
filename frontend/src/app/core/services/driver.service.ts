import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Driver } from '../models/driver.model';
import { ApiResponse } from '../models/api-response.model';

@Injectable({
  providedIn: 'root'
})
export class DriverService {
  private http = inject(HttpClient);
  public apiUrl = `${environment.apiUrl}/choferes`;

  getAllDrivers(): Observable<ApiResponse<Driver[]>> {
    return this.http.get<ApiResponse<Driver[]>>(this.apiUrl);
  }

  getDriverById(id: number): Observable<ApiResponse<Driver>> {
    return this.http.get<ApiResponse<Driver>>(`${this.apiUrl}/${id}`);
  }

  createDriver(driver: Driver): Observable<ApiResponse<Driver>> {
    return this.http.post<ApiResponse<Driver>>(`${this.apiUrl}/guardar`, driver);
  }

  updateDriver(id: number, driver: Driver): Observable<ApiResponse<Driver>> {
    return this.http.put<ApiResponse<Driver>>(`${this.apiUrl}/modificar/${id}`, driver);
  }

  deleteDriver(id: number): Observable<ApiResponse<any>> {
    return this.http.delete<ApiResponse<any>>(`${this.apiUrl}/eliminar/${id}`);
  }
}
