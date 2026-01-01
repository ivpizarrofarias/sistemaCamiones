import { Component, OnInit, inject, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import {
  MatPaginator,
  MatPaginatorModule,
  MatPaginatorIntl
} from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { RouterLink } from '@angular/router';

import { ClientService } from '../../core/services/entities.service';
import { Client } from '../../core/models/entities.model';

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
  selector: 'app-client-list',
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
          <mat-card-title>Clientes</mat-card-title>
        </mat-card-header>

        <mat-card-content>

          <!-- Botón Crear -->
          <div class="header-actions">
            <button mat-raised-button color="primary" routerLink="/clients/create">
              <mat-icon>person_add</mat-icon>
              Crear Cliente
            </button>
          </div>

          <div class="spacer"></div>

          <!-- Buscador -->
          <mat-form-field appearance="outline" class="search-field">
            <mat-label>Buscar cliente</mat-label>
            <input
              matInput
              (keyup)="applyFilter($event)"
              placeholder="Nombre, Email o Teléfono"
              #input
            >
            <mat-icon matSuffix>search</mat-icon>
          </mat-form-field>

          <!-- Tabla -->
          <table mat-table [dataSource]="dataSource" class="mat-elevation-z8">

            <!-- ID -->
            <ng-container matColumnDef="clientId">
              <th mat-header-cell *matHeaderCellDef> ID </th>
              <td mat-cell *matCellDef="let client">
                {{ client.clientId }}
              </td>
            </ng-container>

            <!-- Nombre -->
            <ng-container matColumnDef="clientName">
              <th mat-header-cell *matHeaderCellDef> Nombre </th>
              <td mat-cell *matCellDef="let client">
                {{ client.clientName }}
              </td>
            </ng-container>

            <!-- Email -->
            <ng-container matColumnDef="clientEmail">
              <th mat-header-cell *matHeaderCellDef> Email </th>
              <td mat-cell *matCellDef="let client">
                {{ client.clientEmail }}
              </td>
            </ng-container>

            <!-- Acciones -->
            <ng-container matColumnDef="actions">
              <th mat-header-cell *matHeaderCellDef> Acciones </th>
              <td mat-cell *matCellDef="let client">
                <button
                  mat-icon-button
                  color="primary"
                  [routerLink]="['/clients/edit', client.clientId]"
                >
                  <mat-icon>edit</mat-icon>
                </button>

                <button
                  mat-icon-button
                  color="warn"
                  (click)="deleteClient(client.clientId!)"
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
                  No hay datos que coincidan con "{{ input.value }}"
                </div>
                <ng-template #noData>
                  <div class="no-data">No hay clientes registrados.</div>
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
export class ClientListComponent implements OnInit {

  private clientService = inject(ClientService);
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  dataSource = new MatTableDataSource<Client>([]);
  displayedColumns: string[] = [
    'clientId',
    'clientName',
    'clientEmail',

    'actions'
  ];

  ngOnInit(): void {
    this.loadClients();
  }

  loadClients(): void {
    this.clientService.getAllClients().subscribe({
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
        console.error('Error al obtener clientes:', err);
        this.dataSource.data = [];
      }
    });
  }

  applyFilter(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.dataSource.filter = value.trim().toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  deleteClient(id: number): void {
    if (confirm('¿Está seguro de eliminar este cliente?')) {
      this.clientService.deleteClient(id).subscribe({
        next: () => this.loadClients(),
        error: err => console.error('Error eliminando cliente', err)
      });
    }
  }
}
