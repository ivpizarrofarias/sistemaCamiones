import { Component, OnInit, inject, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule, MatPaginatorIntl } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { RouterLink } from '@angular/router';

import { DriverService } from '../../core/services/driver.service';
import { Driver } from '../../core/models/driver.model';

export class SpanishPaginatorIntl extends MatPaginatorIntl {
  override itemsPerPageLabel = 'Elementos por página:';
  override nextPageLabel     = 'Siguiente página';
  override previousPageLabel = 'Página anterior';
  override firstPageLabel    = 'Primera página';
  override lastPageLabel     = 'Última página';
}

@Component({
  selector: 'app-driver-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    RouterLink
  ],
  providers: [
    { provide: MatPaginatorIntl, useClass: SpanishPaginatorIntl }
  ],
  template: `
    <div class="page-container">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Listado de Conductores</mat-card-title>
        </mat-card-header>

        <mat-card-content>
          <!-- Botón Crear Conductor -->
          <div class="header-actions">
            <button mat-raised-button color="primary" routerLink="/drivers/create">
              <mat-icon>person_add</mat-icon>
              Crear Conductor
            </button>
          </div>

          <div class="spacer"></div>

          <!-- Buscador -->
          <mat-form-field appearance="outline" class="search-field">
            <mat-label>Buscar conductor</mat-label>
            <input matInput (keyup)="applyFilter($event)" placeholder="Nombre, RUT o Licencia" #input>
            <mat-icon matSuffix>search</mat-icon>
          </mat-form-field>

          <!-- Tabla -->
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
                <button mat-icon-button color="primary" [routerLink]="['/drivers/edit', driver.driverId]">
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

          <!-- Paginador fuera de la tabla -->
          <div class="paginator-container">
            <mat-paginator [pageSizeOptions]="[5, 10, 20]" showFirstLastButtons></mat-paginator>
          </div>

        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .page-container { padding: 20px; }
    table { width: 100%; margin-bottom: 10px; }
    .search-field { width: 100%; margin-bottom: 20px; }
    .no-data { padding: 20px; text-align: center; color: #666; }
    .header-actions { display: flex; justify-content: flex-start; margin-bottom: 8px; }
    .spacer { height: 32px; width: 100%; }
    .paginator-container { display: flex; justify-content: flex-end; margin-top: 10px; }
  `]
})
export class DriverListComponent implements OnInit {

  private driverService = inject(DriverService);
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  dataSource = new MatTableDataSource<Driver>([]);
  displayedColumns: string[] = ['id', 'name', 'rut', 'license', 'actions'];

  ngOnInit(): void {
    this.loadDrivers();
  }

  loadDrivers(): void {
    this.driverService.getAllDrivers().subscribe({
      next: (response) => {
        if (response?.data && Array.isArray(response.data)) {
          this.dataSource.data = response.data;
        } else if (Array.isArray(response)) {
          this.dataSource.data = response;
        } else {
          this.dataSource.data = [];
        }

        // Asignar paginador después de cargar datos
        setTimeout(() => this.dataSource.paginator = this.paginator);
      },
      error: (err) => {
        console.error('Error al obtener conductores:', err);
        this.dataSource.data = [];
      }
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator) this.dataSource.paginator.firstPage();
  }

  deleteDriver(id: number): void {
    if (confirm('¿Está seguro de eliminar este conductor?')) {
      this.driverService.deleteDriver(id).subscribe({
        next: () => this.loadDrivers(),
        error: (err) => console.error('Error eliminando conductor', err)
      });
    }
  }
}
