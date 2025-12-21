import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MovementService } from '../../core/services/entities.service';
import { Movement } from '../../core/models/entities.model';

@Component({
  selector: 'app-movement-list',
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
          <mat-card-title>Movimientos</mat-card-title>
          <span class="spacer"></span>
          <button mat-raised-button color="accent" (click)="exportExcel()">
            <mat-icon>download</mat-icon> Exportar Excel
          </button>
        </mat-card-header>
        <mat-card-content>
          <mat-form-field appearance="outline" class="search-field">
            <mat-label>Buscar movimiento</mat-label>
            <input matInput (keyup)="applyFilter($event)" placeholder="Documento, Patente, Chofer o Contenedor" #input>
            <mat-icon matSuffix>search</mat-icon>
          </mat-form-field>

          <table mat-table [dataSource]="dataSource" class="mat-elevation-z8">
            <ng-container matColumnDef="movementId">
              <th mat-header-cell *matHeaderCellDef> ID </th>
              <td mat-cell *matCellDef="let movement"> {{movement.movementId}} </td>
            </ng-container>

            <ng-container matColumnDef="emissionDate">
              <th mat-header-cell *matHeaderCellDef> Emisión </th>
              <td mat-cell *matCellDef="let movement"> {{movement.emissionDate | date:'shortDate'}} </td>
            </ng-container>

            <ng-container matColumnDef="document">
              <th mat-header-cell *matHeaderCellDef> Documento </th>
              <td mat-cell *matCellDef="let movement">
                {{movement.documentType}} - {{movement.documentNumber}}
              </td>
            </ng-container>

            <ng-container matColumnDef="dates">
              <th mat-header-cell *matHeaderCellDef> E/S </th>
              <td mat-cell *matCellDef="let movement">
                <small>Entrada: {{movement.entryDateTime | date:'short'}}</small><br>
                <small>Salida: {{movement.exitDateTime | date:'short'}}</small>
              </td>
            </ng-container>

            <ng-container matColumnDef="details">
              <th mat-header-cell *matHeaderCellDef> Detalles </th>
              <td mat-cell *matCellDef="let movement">
                <small>Camión: {{movement.truckLicensePlate}}</small><br>
                <small>Chofer: {{movement.driverName}}</small><br>
                <small>Contenedor: {{movement.containerCode}}</small>
              </td>
            </ng-container>

            <ng-container matColumnDef="actions">
              <th mat-header-cell *matHeaderCellDef> Acciones </th>
              <td mat-cell *matCellDef="let movement">
                <button mat-icon-button color="primary">
                  <mat-icon>edit</mat-icon>
                </button>
                <button mat-icon-button color="warn" (click)="deleteMovement(movement.movementId)">
                  <mat-icon>delete</mat-icon>
                </button>
              </td>
            </ng-container>

            <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
            <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>

            <tr class="mat-row" *matNoDataRow>
              <td class="mat-cell" colspan="6">
                <div *ngIf="input.value; else noData" class="no-data">
                  No hay datos que coincidan con el filtro "{{input.value}}"
                </div>
                <ng-template #noData>
                  <div class="no-data">No hay movimientos registrados.</div>
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
    .spacer { flex: 1 1 auto; }
    table { width: 100%; }
    .search-field { width: 100%; margin-top: 20px; margin-bottom: 20px; }
    mat-card-header { align-items: center; }
    .no-data { padding: 20px; text-align: center; color: #666; }
  `]
})
export class MovementListComponent implements OnInit {
  private movementService = inject(MovementService);
  dataSource = new MatTableDataSource<Movement>([]);
  displayedColumns: string[] = ['movementId', 'emissionDate', 'document', 'dates', 'details', 'actions'];

  ngOnInit(): void {
    this.loadMovements();
  }

  loadMovements(): void {
    console.log('Cargando movimientos desde:', (this.movementService as any).apiUrl || 'URL no definida');
    this.movementService.getAllMovements().subscribe({
      next: (response: any) => {
        console.log('Respuesta recibida (Movimientos):', response);
        if (Array.isArray(response)) {
          this.dataSource.data = response;
          console.log('Datos asignados (formato array directo):', this.dataSource.data);
        } else if (response && response.success && response.data) {
          this.dataSource.data = response.data;
          console.log('Datos asignados a la tabla (ApiResponse):', this.dataSource.data);
        } else if (response && response.data && Array.isArray(response.data)) {
          this.dataSource.data = response.data;
          console.log('Datos asignados (formato .data directo):', this.dataSource.data);
        } else {
          console.warn('Formato de respuesta no reconocido:', response);
          this.dataSource.data = [];
        }
      },
      error: (err) => {
        console.error('Error al obtener movimientos:', err);
        this.dataSource.data = [];
      }
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  deleteMovement(id: number): void {
    if (confirm('¿Está seguro de eliminar este movimiento?')) {
      this.movementService.deleteMovement(id).subscribe({
        next: () => this.loadMovements(),
        error: (err) => console.error('Error deleting movement', err)
      });
    }
  }

  exportExcel(): void {
    this.movementService.exportExcel();
  }
}
