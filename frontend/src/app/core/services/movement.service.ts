import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { Movement } from '../models/entities.model';
import {
  Client,
  Ship,
  Port,
  GroundTransport,
  Container,
  Trip
} from '../models/entities.model';
import { ApiResponse } from '../models/api-response.model';

@Injectable({
  providedIn: 'root'
})
export class MovementService {

  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/movements`;

  // =====================================================
  // MOVEMENTS (CRUD)
  // =====================================================

  getAllMovements(): Observable<Movement[]> {
    return this.http.get<Movement[]>(this.apiUrl);
  }

  getMovementById(id: number): Observable<Movement> {
    return this.http.get<Movement>(`${this.apiUrl}/${id}`);
  }

  createMovement(movement: Movement): Observable<Movement> {
    return this.http.post<Movement>(this.apiUrl, movement);
  }

  updateMovement(id: number, movement: Movement): Observable<Movement> {
    return this.http.put<Movement>(`${this.apiUrl}/${id}`, movement);
  }

  deleteMovement(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // =====================================================
  // EXPORTACIONES
  // =====================================================

  exportExcel(): void {
    window.open(`${this.apiUrl}/export/excel`, '_blank');
  }

  exportExcelByDriver(driverId: number): void {
    window.open(`${this.apiUrl}/export/excel/driver/${driverId}`, '_blank');
  }

  // =====================================================
  // BÚSQUEDAS Y FILTROS
  // =====================================================

  search(term: string): Observable<Movement[]> {
    return this.http.get<Movement[]>(`${this.apiUrl}/search`, {
      params: { term }
    });
  }

  getByTruck(truckId: number): Observable<Movement[]> {
    return this.http.get<Movement[]>(`${this.apiUrl}/truck/${truckId}`);
  }

  getByDriver(driverId: number): Observable<Movement[]> {
    return this.http.get<Movement[]>(`${this.apiUrl}/driver/${driverId}`);
  }

  getByClient(clientId: number): Observable<Movement[]> {
    return this.http.get<Movement[]>(`${this.apiUrl}/client/${clientId}`);
  }

  getByShip(shipId: number): Observable<Movement[]> {
    return this.http.get<Movement[]>(`${this.apiUrl}/ship/${shipId}`);
  }

  getByPort(portId: number): Observable<Movement[]> {
    return this.http.get<Movement[]>(`${this.apiUrl}/port/${portId}`);
  }

  getByGroundTransport(transportId: number): Observable<Movement[]> {
    return this.http.get<Movement[]>(`${this.apiUrl}/ground-transport/${transportId}`);
  }

  getByContainer(containerId: number): Observable<Movement[]> {
    return this.http.get<Movement[]>(`${this.apiUrl}/container/${containerId}`);
  }

  // =====================================================
  // DATOS PARA SELECTS (FORMULARIOS)
  // =====================================================

  getClients(): Observable<ApiResponse<Client[]>> {
    return this.http.get<ApiResponse<Client[]>>(
      `${environment.apiUrl}/clientes`
    );
  }

  getShips(): Observable<Ship[]> {
    return this.http.get<Ship[]>(
      `${environment.apiUrl}/ships`
    );
  }

  getPorts(): Observable<Port[]> {
    return this.http.get<Port[]>(
      `${environment.apiUrl}/ports`
    );
  }

  getGroundTransports(): Observable<GroundTransport[]> {
    return this.http.get<GroundTransport[]>(
      `${environment.apiUrl}/ground-transports`
    );
  }

  getContainers(): Observable<ApiResponse<Container[]>> {
    return this.http.get<ApiResponse<Container[]>>(
      `${environment.apiUrl}/contenedores`
    );
  }

  getTrips(): Observable<Trip[]> {
    return this.http.get<Trip[]>(
      `${environment.apiUrl}/trips`
    );
  }

  getTrucks(): Observable<any[]> {
    return this.http.get<any[]>(
      `${environment.apiUrl}/trucks`
    );
  }

  getDrivers(): Observable<any[]> {
    return this.http.get<any[]>(
      `${environment.apiUrl}/drivers`
    );
  }
}
