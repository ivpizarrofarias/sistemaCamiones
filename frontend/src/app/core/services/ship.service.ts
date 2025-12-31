import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Ship } from '../models/entities.model';

@Injectable({
  providedIn: 'root'
})
export class ShipService {

  private http = inject(HttpClient);
  public apiUrl = `${environment.apiUrl}/ships`;

  getAllShips(): Observable<Ship[]> {
    console.log('ShipService: Solicitando todas las naves a', this.apiUrl);
    return this.http.get<Ship[]>(this.apiUrl);
  }

  getShipById(id: number): Observable<Ship> {
    return this.http.get<Ship>(`${this.apiUrl}/${id}`);
  }

  createShip(ship: Ship): Observable<Ship> {
    return this.http.post<Ship>(this.apiUrl, ship);
  }

  updateShip(id: number, ship: Ship): Observable<Ship> {
    return this.http.put<Ship>(`${this.apiUrl}/${id}`, ship);
  }

  deleteShip(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
