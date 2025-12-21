import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { DriverService } from '../../core/services/driver.service';
import { Driver } from '../../core/models/driver.model';

@Component({
  selector: 'app-driver-list',
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
          <mat-card-title>Listado de Conductores</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <mat-form-field appearance="outline" class="search-field">
            <mat-label>Buscar conductor</mat-label>
            <input matInput (keyup)="applyFilter($event)" placeholder="Nombre, RUT o Licencia" #input>
            <mat-icon matSuffix>search</mat-icon>
          </mat-form-field>

          <table mat-table [dataSource]="dataSource" class="mat-elevation-z8">
            <ng-container matColumnDef="id">
              <th mat-header-cell *matHeaderCellDef> ID </th>
              <td mat-cell *matCellDef="let driver"> {{driver.driverId}} </td>
            </ng-container>

            <ng-container matColumnDef="name">
              <th mat-header-cell *matHeaderCellDef> Nombre </th>
              <td mat-cell *matCellDef="let driver"> {{driver.driverName}} </td>
            </ng-container>

            <ng-container matColumnDef="rut">
              <th mat-header-cell *matHeaderCellDef> RUT </th>
              <td mat-cell *matCellDef="let driver"> {{driver.driverRut}} </td>
            </ng-container>

            <ng-container matColumnDef="license">
              <th mat-header-cell *matHeaderCellDef> Licencia </th>
              <td mat-cell *matCellDef="let driver"> {{driver.driverLicenseNumber}} </td>
            </ng-container>

            <ng-container matColumnDef="actions">
              <th mat-header-cell *matHeaderCellDef> Acciones </th>
              <td mat-cell *matCellDef="let driver">
                <button mat-icon-button color="primary">
                  <mat-icon>edit</mat-icon>
                </button>
                <button mat-icon-button color="warn" (click)="deleteDriver(driver.driverId)">
                  <mat-icon>delete</mat-icon>
                </button>
              </td>
            </ng-container>

            <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
            <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>

            <tr class="mat-row" *matNoDataRow>
              <td class="mat-cell" colspan="5">
                <div *ngIf="input.value; else noData" class="no-data">
                  No hay datos que coincidan con el filtro "{{input.value}}"
                </div>
                <ng-template #noData>
                  <div class="no-data">No hay conductores registrados.</div>
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
export class DriverListComponent implements OnInit {
  private driverService = inject(DriverService);
  dataSource = new MatTableDataSource<Driver>([]);
  displayedColumns: string[] = ['id', 'name', 'rut', 'license', 'actions'];

  ngOnInit(): void {
    this.loadDrivers();
  }

  loadDrivers(): void {
    console.log('Cargando choferes desde:', this.driverService['apiUrl']);
    this.driverService.getAllDrivers().subscribe({
      next: (response) => {
        console.log('Respuesta recibida (Choferes):', response);
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
        console.error('Error al obtener choferes:', err);
        this.dataSource.data = [];
      }
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  deleteDriver(id: number): void {
    if (confirm('¿Está seguro de eliminar este conductor?')) {
      this.driverService.deleteDriver(id).subscribe({
        next: () => this.loadDrivers(),
        error: (err) => console.error('Error deleting driver', err)
      });
    }
  }
}
