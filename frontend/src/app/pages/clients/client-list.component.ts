import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ClientService } from '../../core/services/entities.service';
import { Client } from '../../core/models/entities.model';

@Component({
  selector: 'app-client-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule
  ],
  template: `
    <div class="page-container">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Clientes</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <mat-form-field appearance="outline" class="search-field">
            <mat-label>Buscar cliente</mat-label>
            <input matInput (keyup)="applyFilter($event)" placeholder="Nombre, Email o Teléfono" #input>
            <mat-icon matSuffix>search</mat-icon>
          </mat-form-field>

          <table mat-table [dataSource]="dataSource" class="mat-elevation-z8">
            <ng-container matColumnDef="clientId">
              <th mat-header-cell *matHeaderCellDef> ID </th>
              <td mat-cell *matCellDef="let client"> {{client.clientId}} </td>
            </ng-container>

            <ng-container matColumnDef="clientName">
              <th mat-header-cell *matHeaderCellDef> Nombre </th>
              <td mat-cell *matCellDef="let client"> {{client.clientName}} </td>
            </ng-container>

            <ng-container matColumnDef="clientEmail">
              <th mat-header-cell *matHeaderCellDef> Email </th>
              <td mat-cell *matCellDef="let client"> {{client.clientEmail}} </td>
            </ng-container>

            <ng-container matColumnDef="clientPhone">
              <th mat-header-cell *matHeaderCellDef> Teléfono </th>
              <td mat-cell *matCellDef="let client"> {{client.clientPhone}} </td>
            </ng-container>

            <ng-container matColumnDef="actions">
              <th mat-header-cell *matHeaderCellDef> Acciones </th>
              <td mat-cell *matCellDef="let client">
                <button mat-icon-button color="primary">
                  <mat-icon>edit</mat-icon>
                </button>
                <button mat-icon-button color="warn" (click)="deleteClient(client.clientId)">
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
                  <div class="no-data">No hay clientes registrados.</div>
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
export class ClientListComponent implements OnInit {
  private clientService = inject(ClientService);
  dataSource = new MatTableDataSource<Client>([]);
  displayedColumns: string[] = ['clientId', 'clientName', 'clientEmail', 'clientPhone', 'actions'];

  ngOnInit(): void {
    this.loadClients();
  }

  loadClients(): void {
    console.log('Cargando clientes desde:', (this.clientService as any).apiUrl || 'URL no definida');
    this.clientService.getAllClients().subscribe({
      next: (response: any) => {
        console.log('Respuesta recibida (Clientes):', response);
        if (response && response.success) {
          this.dataSource.data = response.data;
          console.log('Datos asignados a la tabla:', this.dataSource.data);
        } else if (Array.isArray(response)) {
          this.dataSource.data = response;
          console.log('Datos asignados (formato array directo):', this.dataSource.data);
        } else if (response && response.data && Array.isArray(response.data)) {
          this.dataSource.data = response.data;
          console.log('Datos asignados (formato .data directo):', this.dataSource.data);
        } else {
          console.warn('Formato de respuesta no reconocido:', response);
          this.dataSource.data = [];
        }
      },
      error: (err) => {
        console.error('Error al obtener clientes:', err);
        this.dataSource.data = [];
      }
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  deleteClient(id: number): void {
    if (confirm('¿Está seguro de eliminar este cliente?')) {
      this.clientService.deleteClient(id).subscribe({
        next: (response) => {
          if (response.success) {
            this.loadClients();
          }
        },
        error: (err) => console.error('Error deleting client', err)
      });
    }
  }
}
