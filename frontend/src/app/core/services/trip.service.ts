import {Trip} from "../models/trip.model";
import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";

@Injectable({ providedIn: 'root' })
export class TripService {

  private readonly API_URL = 'http://localhost:8080/api/trips';

  constructor(private http: HttpClient) {}

  createTrip(trip: Trip) {
    return this.http.post<Trip>(this.API_URL, trip);
  }

  getAllTrips() {
    return this.http.get<Trip[]>(this.API_URL);
  }

  getTripById(id: number) {
    return this.http.get<Trip>(`${this.API_URL}/${id}`);
  }

  updateTrip(id: number, trip: Trip) {
    return this.http.put<Trip>(`${this.API_URL}/${id}`, trip);
  }

  deleteTrip(id: number) {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
