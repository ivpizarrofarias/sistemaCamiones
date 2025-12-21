import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { TruckService } from '../../core/services/truck.service';
import { Truck } from '../../core/models/truck.model';

@Component({
  selector: 'app-truck-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule
  ],
  template: `
    <div class="page-container">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Listado de Camiones</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <mat-form-field appearance="outline" class="search-field">
            <mat-label>Buscar camión</mat-label>
            <input matInput (keyup)="applyFilter($event)" placeholder="Ej. AB*CD*12" #input>
            <mat-icon matSuffix>search</mat-icon>
          </mat-form-field>

          <table mat-table [dataSource]="dataSource" class="mat-elevation-z8">
            <ng-container matColumnDef="id">
              <th mat-header-cell *matHeaderCellDef> ID </th>
              <td mat-cell *matCellDef="let truck"> {{truck.truckId}} </td>
            </ng-container>

            <ng-container matColumnDef="licensePlate">
              <th mat-header-cell *matHeaderCellDef> Patente </th>
              <td mat-cell *matCellDef="let truck"> {{truck.licensePlate}} </td>
            </ng-container>

            <ng-container matColumnDef="actions">
              <th mat-header-cell *matHeaderCellDef> Acciones </th>
              <td mat-cell *matCellDef="let truck">
                <button mat-icon-button color="primary">
                  <mat-icon>edit</mat-icon>
                </button>
                <button mat-icon-button color="warn" (click)="deleteTruck(truck.truckId)">
                  <mat-icon>delete</mat-icon>
                </button>
              </td>
            </ng-container>

            <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
            <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>

            <tr class="mat-row" *matNoDataRow>
              <td class="mat-cell" colspan="3">
                <div *ngIf="input.value; else noData" class="no-data">
                  No hay datos que coincidan con el filtro "{{input.value}}"
                </div>
                <ng-template #noData>
                  <div class="no-data">No hay camiones registrados.</div>
                </ng-template>
              </td>
            </tr>
          </table>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .page-container { padding: 20px; }
    table { width: 100%; }
    .search-field { width: 100%; margin-bottom: 20px; }
    .no-data { padding: 20px; text-align: center; color: #666; }
  `]
})
export class TruckListComponent implements OnInit {
  private truckService = inject(TruckService);
  dataSource = new MatTableDataSource<Truck>([]);
  displayedColumns: string[] = ['id', 'licensePlate', 'actions'];

  ngOnInit(): void {
    this.loadTrucks();
  }

  loadTrucks(): void {
    console.log('Cargando camiones desde:', this.truckService['apiUrl']);
    this.truckService.getAllTrucks().subscribe({
      next: (response) => {
        console.log('Respuesta recibida (Camiones):', response);
        if (response && response.data) {
          this.dataSource.data = response.data;
          console.log('Datos asignados a la tabla:', this.dataSource.data);
        } else if (Array.isArray(response)) {
          this.dataSource.data = response;
          console.log('Datos asignados (formato array directo):', this.dataSource.data);
        } else {
          console.warn('Formato de respuesta no reconocido:', response);
          this.dataSource.data = [];
        }
      },
      error: (err) => {
        console.error('Error al obtener camiones:', err);
        this.dataSource.data = [];
      }
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  deleteTruck(id: number): void {
    if (confirm('¿Está seguro de eliminar este camión?')) {
      this.truckService.deleteTruck(id).subscribe({
        next: () => this.loadTrucks(),
        error: (err) => console.error('Error deleting truck', err)
      });
    }
  }
}
