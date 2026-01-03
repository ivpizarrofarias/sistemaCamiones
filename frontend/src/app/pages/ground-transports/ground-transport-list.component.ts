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

import { GroundTransportService } from '../../core/services/entities.service';
import { GroundTransport } from '../../core/models/entities.model';

/* =========================
   Paginador en Español
========================= */
export class SpanishPaginatorIntl extends MatPaginatorIntl {
  override itemsPerPageLabel = 'Elementos por página:';
  override nextPageLabel = 'Siguiente página';
  override previousPageLabel = 'Página anterior';
  override firstPageLabel = 'Primera página';
  override lastPageLabel = 'Última página';
}

@Component({
  selector: 'app-ground-transport-list',
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
          <mat-card-title>Transportes Terrestres</mat-card-title>
        </mat-card-header>

        <mat-card-content>

          <!-- Botón Crear -->
          <div class="header-actions">
            <button mat-raised-button color="primary" routerLink="/ground-transports/create">
              <mat-icon>add</mat-icon>
              Crear Transporte
            </button>
          </div>

          <div class="spacer"></div>

          <!-- Buscador -->
          <mat-form-field appearance="outline" class="search-field">
            <mat-label>Buscar transporte</mat-label>
            <input
              matInput
              (keyup)="applyFilter($event)"
              placeholder="Nombre del transportista"
              #input
            >
            <mat-icon matSuffix>search</mat-icon>
          </mat-form-field>

          <!-- Tabla -->
          <table mat-table [dataSource]="dataSource" class="mat-elevation-z8">

            <!-- ID -->
            <ng-container matColumnDef="transportId">
              <th mat-header-cell *matHeaderCellDef> ID </th>
              <td mat-cell *matCellDef="let transport">
                {{ transport.transportId }}
              </td>
            </ng-container>

            <!-- Nombre -->
            <ng-container matColumnDef="transporterName">
              <th mat-header-cell *matHeaderCellDef> Transportista </th>
              <td mat-cell *matCellDef="let transport">
                {{ transport.transporterName }}
              </td>
            </ng-container>

            <!-- Acciones -->
            <ng-container matColumnDef="actions">
              <th mat-header-cell *matHeaderCellDef> Acciones </th>
              <td mat-cell *matCellDef="let transport">

                <button
                  mat-icon-button
                  color="primary"
                  [routerLink]="['/ground-transports/edit', transport.transportId]"
                >
                  <mat-icon>edit</mat-icon>
                </button>

                <button
                  mat-icon-button
                  color="warn"
                  (click)="deleteTransport(transport.transportId!)"
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
                  <div class="no-data">No hay transportes registrados.</div>
                </ng-template>
              </td>
            </tr>

          </table>

          <!-- Paginador -->
          <div class="paginator-container">
            <mat-paginator
              [pageSizeOptions]="[5, 10, 20]"
              showFirstLastButtons>
            </mat-paginator>
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
export class GroundTransportListComponent implements OnInit {

  private transportService = inject(GroundTransportService);

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  dataSource = new MatTableDataSource<GroundTransport>([]);
  displayedColumns: string[] = ['transportId', 'transporterName', 'actions'];

  ngOnInit(): void {
    this.loadTransports();
  }

  loadTransports(): void {
    this.transportService.getAllGroundTransports().subscribe({
      next: (response: any) => {

        if (response?.data && Array.isArray(response.data)) {
          this.dataSource.data = response.data;
        } else if (Array.isArray(response)) {
          this.dataSource.data = response;
        } else {
          this.dataSource.data = [];
        }

        setTimeout(() => {
          this.dataSource.paginator = this.paginator;
        });
      },
      error: err => {
        console.error('Error al obtener transportes:', err);
        this.dataSource.data = [];
      }
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  deleteTransport(id: number): void {
    if (confirm('¿Está seguro de eliminar este transporte?')) {
      this.transportService.deleteGroundTransport(id).subscribe({
        next: () => this.loadTransports(),
        error: err => console.error('Error eliminando transporte', err)
      });
    }
  }
}
