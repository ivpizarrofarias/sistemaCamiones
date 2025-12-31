import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { RouterLink } from '@angular/router';

import { ShipService } from '../../core/services/entities.service';
import { Ship } from '../../core/models/entities.model';

@Component({
  selector: 'app-ship-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    RouterLink
  ],
  template: `
    <div class="page-container">
      <mat-card>

        <mat-card-header class="header">
          <mat-card-title>Naves</mat-card-title>
        </mat-card-header>

        <mat-card-content>

          <!-- Botón Crear -->
          <div class="header-actions">
            <button mat-raised-button color="primary" routerLink="/ships/create">
              <mat-icon>add</mat-icon>
              Crear Nave
            </button>
          </div>

          <div class="spacer"></div>

          <!-- Buscador -->
          <mat-form-field appearance="outline" class="search-field">
            <mat-label>Buscar nave</mat-label>
            <input
              matInput
              (keyup)="applyFilter($event)"
              placeholder="Nombre, Nro Viaje o Línea"
              #input
            >
            <mat-icon matSuffix>search</mat-icon>
          </mat-form-field>

          <!-- Tabla -->
          <table mat-table [dataSource]="dataSource" class="mat-elevation-z8">

            <!-- ID -->
            <ng-container matColumnDef="shipId">
              <th mat-header-cell *matHeaderCellDef> ID </th>
              <td mat-cell *matCellDef="let ship"> {{ ship.shipId }} </td>
            </ng-container>

            <!-- Nombre -->
            <ng-container matColumnDef="shipName">
              <th mat-header-cell *matHeaderCellDef> Nombre </th>
              <td mat-cell *matCellDef="let ship"> {{ ship.shipName }} </td>
            </ng-container>

            <!-- Nro Viaje -->
            <ng-container matColumnDef="voyageNumber">
              <th mat-header-cell *matHeaderCellDef> Nro Viaje </th>
              <td mat-cell *matCellDef="let ship"> {{ ship.voyageNumber }} </td>
            </ng-container>

            <!-- Línea -->
            <ng-container matColumnDef="shippingLine">
              <th mat-header-cell *matHeaderCellDef> Línea Naviera </th>
              <td mat-cell *matCellDef="let ship"> {{ ship.shippingLine }} </td>
            </ng-container>

            <!-- Acciones -->
            <ng-container matColumnDef="actions">
              <th mat-header-cell *matHeaderCellDef> Acciones </th>
              <td mat-cell *matCellDef="let ship">
                <button
                  mat-icon-button
                  color="primary"
                  [routerLink]="['/ships/edit', ship.shipId]"
                >
                  <mat-icon>edit</mat-icon>
                </button>
                <button
                  mat-icon-button
                  color="warn"
                  (click)="deleteShip(ship.shipId)"
                >
                  <mat-icon>delete</mat-icon>
                </button>
              </td>
            </ng-container>

            <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
            <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>

            <!-- Sin datos -->
            <tr class="mat-row" *matNoDataRow>
              <td class="mat-cell" colspan="5">
                <div *ngIf="input.value; else noData" class="no-data">
                  No hay datos que coincidan con el filtro "{{ input.value }}"
                </div>
                <ng-template #noData>
                  <div class="no-data">No hay naves registradas.</div>
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
export class ShipListComponent implements OnInit {

  private shipService = inject(ShipService);

  dataSource = new MatTableDataSource<Ship>([]);
  displayedColumns: string[] = [
    'shipId',
    'shipName',
    'voyageNumber',
    'shippingLine',
    'actions'
  ];

  ngOnInit(): void {
    this.loadShips();
  }

  loadShips(): void {
    console.log('ShipListComponent: Cargando naves...');
    this.shipService.getAllShips().subscribe({
      next: (response: any) => {
        console.log('ShipListComponent: Respuesta:', response);

        if (response && response.success && Array.isArray(response.data)) {
          this.dataSource.data = response.data;
        } else if (Array.isArray(response)) {
          this.dataSource.data = response;
        } else if (response?.data && Array.isArray(response.data)) {
          this.dataSource.data = response.data;
        } else {
          console.warn('Formato de respuesta no reconocido', response);
          this.dataSource.data = [];
        }
      },
      error: (err) => {
        console.error('Error al obtener naves', err);
        this.dataSource.data = [];
      }
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  deleteShip(id: number): void {
    if (confirm('¿Está seguro de eliminar esta nave?')) {
      this.shipService.deleteShip(id).subscribe({
        next: () => this.loadShips(),
        error: (err) => console.error('Error eliminando nave', err)
      });
    }
  }
}
