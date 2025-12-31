import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';

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
    MatIconModule,
    RouterLink
  ],
  template: `
    <div class="page-container">
      <mat-card>

        <mat-card-header>
          <mat-card-title>Listado de Camiones</mat-card-title>
        </mat-card-header>

        <mat-card-content>

          <!-- Botón Crear Camión -->
          <div class="header-actions">
            <button
              mat-raised-button
              color="primary"
              routerLink="/trucks/create"
            >
              <mat-icon>add</mat-icon>
              Crear Camión
            </button>
          </div>

          <div class="spacer"></div>

          <!-- Buscador -->
          <mat-form-field appearance="outline" class="search-field">
            <mat-label>Buscar camión</mat-label>
            <input
              matInput
              (keyup)="applyFilter($event)"
              placeholder="Ej. AB*CD*12"
              #input
            >
            <mat-icon matSuffix>search</mat-icon>
          </mat-form-field>

          <!-- Tabla -->
          <table mat-table [dataSource]="dataSource" class="mat-elevation-z8">

            <!-- ID -->
            <ng-container matColumnDef="id">
              <th mat-header-cell *matHeaderCellDef> ID </th>
              <td mat-cell *matCellDef="let truck"> {{ truck.truckId }} </td>
            </ng-container>

            <!-- Patente -->
            <ng-container matColumnDef="licensePlate">
              <th mat-header-cell *matHeaderCellDef> Patente </th>
              <td mat-cell *matCellDef="let truck"> {{ truck.licensePlate }} </td>
            </ng-container>

            <!-- Acciones -->
            <ng-container matColumnDef="actions">
              <th mat-header-cell *matHeaderCellDef> Acciones </th>
              <td mat-cell *matCellDef="let truck">

                <button
                  mat-icon-button
                  color="warn"
                  (click)="deleteTruck(truck.truckId)"
                >
                  <mat-icon>delete</mat-icon>
                </button>
              </td>
            </ng-container>

            <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
            <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>

            <!-- Sin datos -->
            <tr class="mat-row" *matNoDataRow>
              <td class="mat-cell" colspan="3">
                <div *ngIf="input.value; else noData" class="no-data">
                  No hay datos que coincidan con el filtro "{{ input.value }}"
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
    .page-container {
      padding: 20px;
    }

    table {
      width: 100%;
    }

    .search-field {
      width: 100%;
      margin-bottom: 20px;
    }

    .no-data {
      padding: 20px;
      text-align: center;
      color: #666;
    }

    .header-actions {
      display: flex;
      justify-content: flex-start;
      margin-bottom: 8px;
    }

    .spacer {
      height: 32px;
      width: 100%;
    }
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
    console.log('Cargando camiones...');
    this.truckService.getAllTrucks().subscribe({
      next: (response) => {
        if (response?.data && Array.isArray(response.data)) {
          this.dataSource.data = response.data;
        } else if (Array.isArray(response)) {
          this.dataSource.data = response;
        } else {
          this.dataSource.data = [];
        }
      },
      error: (err) => {
        console.error('Error al obtener camiones:', err);
        this.dataSource.data = [];
      }
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  deleteTruck(id: number): void {
    if (confirm('¿Está seguro de eliminar este camión?')) {
      this.truckService.deleteTruck(id).subscribe({
        next: () => this.loadTrucks(),
        error: (err) => console.error('Error eliminando camión', err)
      });
    }
  }
}
