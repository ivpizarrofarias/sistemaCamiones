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

import { TruckService } from '../../core/services/truck.service';
import { Truck } from '../../core/models/truck.model';

export class SpanishPaginatorIntl extends MatPaginatorIntl {
  override itemsPerPageLabel = 'Elementos por página:';
  override nextPageLabel     = 'Siguiente página';
  override previousPageLabel = 'Página anterior';
  override firstPageLabel    = 'Primera página';
  override lastPageLabel     = 'Última página';
}

@Component({
  selector: 'app-truck-list',
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
          <mat-card-title>Listado de Camiones</mat-card-title>
        </mat-card-header>

        <mat-card-content>
          <!-- Botón Crear Camión -->
          <div class="header-actions">
            <button mat-raised-button color="primary" routerLink="/trucks/create">
              <mat-icon>add</mat-icon>
              Crear Camión
            </button>
          </div>

          <div class="spacer"></div>

          <!-- Buscador -->
          <mat-form-field appearance="outline" class="search-field">
            <mat-label>Buscar camión</mat-label>
            <input matInput (keyup)="applyFilter($event)" placeholder="Patente, ID" #input>
            <mat-icon matSuffix>search</mat-icon>
          </mat-form-field>

          <!-- Tabla -->
          <table mat-table [dataSource]="dataSource" class="mat-elevation-z8">

            <ng-container matColumnDef="id">
              <th mat-header-cell *matHeaderCellDef> ID </th>
              <td mat-cell *matCellDef="let truck"> {{ truck.truckId }} </td>
            </ng-container>

            <ng-container matColumnDef="licensePlate">
              <th mat-header-cell *matHeaderCellDef> Patente </th>
              <td mat-cell *matCellDef="let truck"> {{ truck.licensePlate }} </td>
            </ng-container>

            <ng-container matColumnDef="actions">
              <th mat-header-cell *matHeaderCellDef> Acciones </th>
              <td mat-cell *matCellDef="let truck">
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
export class TruckListComponent implements OnInit {

  private truckService = inject(TruckService);
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  dataSource = new MatTableDataSource<Truck>([]);
  displayedColumns: string[] = ['id', 'licensePlate', 'actions'];

  ngOnInit(): void {
    this.loadTrucks();
  }

  loadTrucks(): void {
    this.truckService.getAllTrucks().subscribe({
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
        console.error('Error al obtener camiones:', err);
        this.dataSource.data = [];
      }
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator) this.dataSource.paginator.firstPage();
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
