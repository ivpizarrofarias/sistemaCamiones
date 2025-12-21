import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ContainerService } from '../../core/services/entities.service';
import { Container } from '../../core/models/entities.model';

@Component({
  selector: 'app-container-list',
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
          <mat-card-title>Contenedores</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <mat-form-field appearance="outline" class="search-field">
            <mat-label>Buscar contenedor</mat-label>
            <input matInput (keyup)="applyFilter($event)" placeholder="Ej. ABCD1234567" #input>
            <mat-icon matSuffix>search</mat-icon>
          </mat-form-field>

          <table mat-table [dataSource]="dataSource" class="mat-elevation-z8">
            <ng-container matColumnDef="containerId">
              <th mat-header-cell *matHeaderCellDef> ID </th>
              <td mat-cell *matCellDef="let container"> {{container.containerId}} </td>
            </ng-container>

            <ng-container matColumnDef="containerCode">
              <th mat-header-cell *matHeaderCellDef> Código </th>
              <td mat-cell *matCellDef="let container"> {{container.containerCode}} </td>
            </ng-container>

            <ng-container matColumnDef="actions">
              <th mat-header-cell *matHeaderCellDef> Acciones </th>
              <td mat-cell *matCellDef="let container">
                <button mat-icon-button color="primary">
                  <mat-icon>edit</mat-icon>
                </button>
                <button mat-icon-button color="warn" (click)="deleteContainer(container.containerId)">
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
                  <div class="no-data">No hay contenedores registrados.</div>
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
export class ContainerListComponent implements OnInit {
  private containerService = inject(ContainerService);
  dataSource = new MatTableDataSource<Container>([]);
  displayedColumns: string[] = ['containerId', 'containerCode', 'actions'];

  ngOnInit(): void {
    this.loadContainers();
  }

  loadContainers(): void {
    console.log('Cargando contenedores desde:', (this.containerService as any).apiUrl || 'URL no definida');
    this.containerService.getAllContainers().subscribe({
      next: (response: any) => {
        console.log('Respuesta recibida (Contenedores):', response);
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
        console.error('Error al obtener contenedores:', err);
        this.dataSource.data = [];
      }
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  deleteContainer(id: number): void {
    if (confirm('¿Está seguro de eliminar este contenedor?')) {
      this.containerService.deleteContainer(id).subscribe({
        next: (response) => {
          if (response.success) {
            this.loadContainers();
          }
        },
        error: (err) => console.error('Error deleting container', err)
      });
    }
  }
}
