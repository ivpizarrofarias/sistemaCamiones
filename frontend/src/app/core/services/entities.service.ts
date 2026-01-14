import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { User, Trip, Ship, Port, Movement, GroundTransport, Container, Client } from '../models/entities.model';
import { ApiResponse } from '../models/api-response.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private http = inject(HttpClient);
  public apiUrl = `${environment.apiUrl}/usuarios`;

  getAllUsers(): Observable<ApiResponse<User[]>> {
    console.log('UserService: Solicitando todos los usuarios a', this.apiUrl);
    return this.http.get<ApiResponse<User[]>>(this.apiUrl);
  }

  getUserById(id: number): Observable<ApiResponse<User>> {
    return this.http.get<ApiResponse<User>>(`${this.apiUrl}/${id}`);
  }

  createUser(user: User): Observable<ApiResponse<User>> {
    return this.http.post<ApiResponse<User>>(`${this.apiUrl}/guardar`, user);
  }

  updateUser(id: number, user: User): Observable<ApiResponse<User>> {
    return this.http.put<ApiResponse<User>>(`${this.apiUrl}/modificar/${id}`, user);
  }

  deleteUser(id: number): Observable<ApiResponse<any>> {
    return this.http.delete<ApiResponse<any>>(`${this.apiUrl}/eliminar/${id}`);
  }
}

@Injectable({
  providedIn: 'root'
})
export class TripService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/trips`;

  getAllTrips(): Observable<Trip[]> {
    return this.http.get<Trip[]>(this.apiUrl);
  }

  getTripById(id: number): Observable<Trip> {
    return this.http.get<Trip>(`${this.apiUrl}/${id}`);
  }

  createTrip(trip: Trip): Observable<Trip> {
    return this.http.post<Trip>(this.apiUrl, trip);
  }

  updateTrip(id: number, trip: Trip): Observable<Trip> {
    return this.http.put<Trip>(`${this.apiUrl}/${id}`, trip);
  }

  deleteTrip(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  searchTrips(origin: string): Observable<Trip[]> {
    return this.http.get<Trip[]>(`${this.apiUrl}/search`, { params: { origin } });
  }
}

@Injectable({
  providedIn: 'root'
})
export class ShipService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/ships`;

  getAllShips(): Observable<Ship[]> {
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

@Injectable({
  providedIn: 'root'
})
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

@Injectable({
  providedIn: 'root'
})
export class MovementService {

  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/movements`;

  // ==========================
  // MOVEMENTS
  // ==========================

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

  exportExcel(): void {
    window.open(`${this.apiUrl}/export/excel`, '_blank');
  }

  // ==========================
  // DATOS PARA SELECTS
  // ==========================

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

@Injectable({
  providedIn: 'root'
})
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

@Injectable({
  providedIn: 'root'
})
export class ContainerService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/contenedores`;

  getAllContainers(): Observable<ApiResponse<Container[]>> {
    return this.http.get<ApiResponse<Container[]>>(this.apiUrl);
  }

  getContainerById(id: number): Observable<ApiResponse<Container>> {
    return this.http.get<ApiResponse<Container>>(`${this.apiUrl}/${id}`);
  }

  createContainer(container: Container): Observable<ApiResponse<Container>> {
    return this.http.post<ApiResponse<Container>>(`${this.apiUrl}/guardar`, container);
  }

  updateContainer(id: number, container: Container): Observable<ApiResponse<Container>> {
    return this.http.put<ApiResponse<Container>>(`${this.apiUrl}/modificar/${id}`, container);
  }

  deleteContainer(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/eliminar/${id}`);
  }
}

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/clientes`;

  getAllClients(): Observable<ApiResponse<Client[]>> {
    return this.http.get<ApiResponse<Client[]>>(this.apiUrl);
  }

  getClientById(id: number): Observable<ApiResponse<Client>> {
    return this.http.get<ApiResponse<Client>>(`${this.apiUrl}/${id}`);
  }

  createClient(client: Client): Observable<ApiResponse<Client>> {
    return this.http.post<ApiResponse<Client>>(`${this.apiUrl}/guardar`, client);
  }

  updateClient(id: number, client: Client): Observable<ApiResponse<Client>> {
    return this.http.put<ApiResponse<Client>>(`${this.apiUrl}/modificar/${id}`, client);
  }

  deleteClient(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/eliminar/${id}`);
  }


}


