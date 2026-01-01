import {inject} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";
import {ApiResponse} from "../models/api-response.model";
import {Container} from "../models/entities.model";

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
