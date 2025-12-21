import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ValuationService } from '../../core/services/valuation.service';
import { Valuation } from '../../core/models/valuation.model';

@Component({
  selector: 'app-valuation-list',
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
    <div class="valuation-list-container">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Listado de Valoraciones</mat-card-title>
          <span class="spacer"></span>
          <button mat-raised-button color="accent" (click)="exportExcel()">
            <mat-icon>download</mat-icon> Exportar Excel
          </button>
        </mat-card-header>
        <mat-card-content>
          <mat-form-field appearance="outline" class="search-field">
            <mat-label>Buscar valoración</mat-label>
            <input matInput (keyup)="applyFilter($event)" placeholder="Cliente, Nave, Viaje o Valor" #input>
            <mat-icon matSuffix>search</mat-icon>
          </mat-form-field>

          <table mat-table [dataSource]="dataSource" class="mat-elevation-z8">
            <ng-container matColumnDef="id">
              <th mat-header-cell *matHeaderCellDef> ID </th>
              <td mat-cell *matCellDef="let valuation"> {{valuation.valuationId}} </td>
            </ng-container>

            <ng-container matColumnDef="date">
              <th mat-header-cell *matHeaderCellDef> Fecha </th>
              <td mat-cell *matCellDef="let valuation"> {{valuation.valuationDate | date:'dd/MM/yyyy HH:mm'}} </td>
            </ng-container>

            <ng-container matColumnDef="client">
              <th mat-header-cell *matHeaderCellDef> Cliente </th>
              <td mat-cell *matCellDef="let valuation"> {{valuation.clientName}} </td>
            </ng-container>

            <ng-container matColumnDef="ship">
              <th mat-header-cell *matHeaderCellDef> Nave </th>
              <td mat-cell *matCellDef="let valuation"> {{valuation.shipName}} </td>
            </ng-container>

            <ng-container matColumnDef="value">
              <th mat-header-cell *matHeaderCellDef> Valor </th>
              <td mat-cell *matCellDef="let valuation"> {{valuation.value}} </td>
            </ng-container>

            <ng-container matColumnDef="trip">
              <th mat-header-cell *matHeaderCellDef> Viaje </th>
              <td mat-cell *matCellDef="let valuation"> {{valuation.tripCode}} </td>
            </ng-container>

            <ng-container matColumnDef="actions">
              <th mat-header-cell *matHeaderCellDef> Acciones </th>
              <td mat-cell *matCellDef="let valuation">
                <button mat-icon-button color="primary">
                  <mat-icon>edit</mat-icon>
                </button>
                <button mat-icon-button color="warn" (click)="deleteValuation(valuation.valuationId)">
                  <mat-icon>delete</mat-icon>
                </button>
              </td>
            </ng-container>

            <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
            <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>

            <tr class="mat-row" *matNoDataRow>
              <td class="mat-cell" colspan="7">
                <div *ngIf="input.value; else noData" class="no-data">
                  No hay datos que coincidan con el filtro "{{input.value}}"
                </div>
                <ng-template #noData>
                  <div class="no-data">No hay valoraciones registradas.</div>
                </ng-template>
              </td>
            </tr>
          </table>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .valuation-list-container {
      padding: 20px;
    }
    .spacer {
      flex: 1 1 auto;
    }
    table {
      width: 100%;
    }
    .search-field { width: 100%; margin-top: 20px; margin-bottom: 20px; }
    mat-card-header {
      display: flex;
      align-items: center;
      margin-bottom: 20px;
    }
    .no-data { padding: 20px; text-align: center; color: #666; }
  `]
})
export class ValuationListComponent implements OnInit {
  private valuationService = inject(ValuationService);

  dataSource = new MatTableDataSource<Valuation>([]);
  displayedColumns: string[] = ['id', 'date', 'client', 'ship', 'trip', 'value', 'actions'];

  ngOnInit(): void {
    this.loadValuations();
  }

  loadValuations(): void {
    console.log('Cargando valoraciones desde:', (this.valuationService as any).apiUrl || 'URL no definida');
    this.valuationService.getAllValuations().subscribe({
      next: (response: any) => {
        console.log('Respuesta recibida (Valoraciones):', response);
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
        console.error('Error al obtener valoraciones:', err);
        this.dataSource.data = [];
      }
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  deleteValuation(id: number): void {
    if (confirm('¿Está seguro de eliminar esta valoración?')) {
      this.valuationService.deleteValuationById(id).subscribe({
        next: () => {
          this.loadValuations();
        },
        error: (err) => {
          console.error('Error deleting valuation', err);
        }
      });
    }
  }

  exportExcel(): void {
    this.valuationService.exportExcel();
  }
}
