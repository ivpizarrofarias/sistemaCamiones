import { Component, OnInit, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule, MatPaginatorIntl } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { RouterLink } from '@angular/router';

import { MovementService } from '../../core/services/movement.service';
import { Movement } from '../../core/models/movement.model';

/* ================= PAGINADOR EN ESPAÑOL ================= */
export class SpanishPaginatorIntl extends MatPaginatorIntl {
  override itemsPerPageLabel = 'Elementos por página';
  override nextPageLabel = 'Siguiente página';
  override previousPageLabel = 'Página anterior';
  override firstPageLabel = 'Primera página';
  override lastPageLabel = 'Última página';
}

@Component({
  selector: 'app-movement-list',
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
          <mat-card-title>Listado de Movimientos</mat-card-title>
        </mat-card-header>

        <mat-card-content>

          <!-- ================= BOTONES SUPERIORES ================= -->
          <div class="header-actions">
            <div class="left-actions">
              <button mat-raised-button color="primary" routerLink="/movements/create">
                <mat-icon>add</mat-icon>
                Crear Movimiento
              </button>

              <button mat-stroked-button color="accent" (click)="exportExcel()">
                <mat-icon>download</mat-icon>
                Exportar Excel
              </button>

              <button mat-stroked-button color="warn" (click)="exportByDriver()">
                <mat-icon>person</mat-icon>
                Exportar por Chofer
              </button>
            </div>
          </div>

          <div class="spacer"></div>

          <!-- ================= BUSCADOR ================= -->
          <mat-form-field appearance="outline" class="search-field">
            <mat-label>Buscar movimiento</mat-label>
            <input matInput (keyup)="applyFilter($event)"
                   placeholder="Documento, chofer, contenedor, cliente"
                   #input>
            <mat-icon matSuffix>search</mat-icon>
          </mat-form-field>

          <!-- ================= TABLA ================= -->
          <table mat-table [dataSource]="dataSource" class="mat-elevation-z8">

            <ng-container matColumnDef="emissionDate">
              <th mat-header-cell *matHeaderCellDef> Fecha Emisión </th>
              <td mat-cell *matCellDef="let m">
                {{ m.emissionDate | date:'dd/MM/yyyy' }}
              </td>
            </ng-container>

            <ng-container matColumnDef="ship">
              <th mat-header-cell *matHeaderCellDef> Nave </th>
              <td mat-cell *matCellDef="let m"> {{ m.shipName }} </td>
            </ng-container>

            <ng-container matColumnDef="documentNumber">
              <th mat-header-cell *matHeaderCellDef> N° Documento </th>
              <td mat-cell *matCellDef="let m"> {{ m.documentNumber }} </td>
            </ng-container>

            <ng-container matColumnDef="documentType">
              <th mat-header-cell *matHeaderCellDef> Tipo Documento </th>
              <td mat-cell *matCellDef="let m"> {{ m.documentType }} </td>
            </ng-container>

            <ng-container matColumnDef="client">
              <th mat-header-cell *matHeaderCellDef> Cliente </th>
              <td mat-cell *matCellDef="let m"> {{ m.clientName }} </td>
            </ng-container>

            <ng-container matColumnDef="transport">
              <th mat-header-cell *matHeaderCellDef> Transporte </th>
              <td mat-cell *matCellDef="let m"> {{ m.transportName }} </td>
            </ng-container>

            <ng-container matColumnDef="truckPlate">
              <th mat-header-cell *matHeaderCellDef> Patente </th>
              <td mat-cell *matCellDef="let m"> {{ m.truckLicensePlate }} </td>
            </ng-container>

            <ng-container matColumnDef="driver">
              <th mat-header-cell *matHeaderCellDef> Chofer </th>
              <td mat-cell *matCellDef="let m"> {{ m.driverName }} </td>
            </ng-container>

            <ng-container matColumnDef="container">
              <th mat-header-cell *matHeaderCellDef> Contenedor </th>
              <td mat-cell *matCellDef="let m"> {{ m.containerCode }} </td>
            </ng-container>

            <ng-container matColumnDef="size">
              <th mat-header-cell *matHeaderCellDef> Tamaño </th>
              <td mat-cell *matCellDef="let m"> {{ m.sizeType }} </td>
            </ng-container>

            <ng-container matColumnDef="status">
              <th mat-header-cell *matHeaderCellDef> Estado </th>
              <td mat-cell *matCellDef="let m"> {{ m.physicalState }} </td>
            </ng-container>

            <ng-container matColumnDef="origin">
              <th mat-header-cell *matHeaderCellDef> Origen </th>
              <td mat-cell *matCellDef="let m"> {{ m.origin }} </td>
            </ng-container>

            <ng-container matColumnDef="destination">
              <th mat-header-cell *matHeaderCellDef> Destino </th>
              <td mat-cell *matCellDef="let m"> {{ m.destination }} </td>
            </ng-container>

            <ng-container matColumnDef="entryDate">
              <th mat-header-cell *matHeaderCellDef> Entrada </th>
              <td mat-cell *matCellDef="let m">
                {{ m.entryDateTime | date:'dd/MM/yyyy HH:mm' }}
              </td>
            </ng-container>

            <ng-container matColumnDef="exitDate">
              <th mat-header-cell *matHeaderCellDef> Salida </th>
              <td mat-cell *matCellDef="let m">
                {{ m.exitDateTime | date:'dd/MM/yyyy HH:mm' }}
              </td>
            </ng-container>

            <ng-container matColumnDef="actions">
              <th mat-header-cell *matHeaderCellDef> Acciones </th>
              <td mat-cell *matCellDef="let m">
                <button mat-icon-button color="primary"
                        [routerLink]="['/movements/edit', m.movementId]">
                  <mat-icon>edit</mat-icon>
                </button>
                <button mat-icon-button color="warn"
                        (click)="deleteMovement(m.movementId!)">
                  <mat-icon>delete</mat-icon>
                </button>
              </td>
            </ng-container>

            <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
            <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>

            <tr matNoDataRow>
              <td colspan="16" class="no-data">
                No hay movimientos registrados
              </td>
            </tr>
          </table>

          <!-- ================= PAGINADOR ================= -->
          <div class="paginator-container">
            <mat-paginator
              [pageSizeOptions]="[5,10,20]"
              showFirstLastButtons>
            </mat-paginator>
          </div>

        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .page-container { padding: 20px; }
    table { width: 100%; }
    .search-field { width: 100%; margin-bottom: 20px; }
    .header-actions { display: flex; justify-content: space-between; }
    .left-actions { display: flex; gap: 12px; }
    .spacer { height: 24px; }
    .no-data { text-align: center; padding: 20px; color: #666; }
    .paginator-container { display: flex; justify-content: flex-end; margin-top: 12px; }
  `]
})
export class MovementListComponent implements OnInit {

  private movementService = inject(MovementService);

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  dataSource = new MatTableDataSource<Movement>([]);

  displayedColumns: string[] = [
    'emissionDate',
    'ship',
    'documentNumber',
    'documentType',
    'client',
    'transport',
    'truckPlate',
    'driver',
    'container',
    'size',
    'status',
    'origin',
    'destination',
    'entryDate',
    'exitDate',
    'actions'
  ];

  ngOnInit(): void {
    this.loadMovements();
  }

  loadMovements(): void {
    this.movementService.getAllMovements().subscribe({
      next: (res: any) => {
        if (res?.data && Array.isArray(res.data)) {
          this.dataSource.data = res.data;
        } else if (Array.isArray(res)) {
          this.dataSource.data = res;
        } else {
          this.dataSource.data = [];
        }

        setTimeout(() => {
          this.dataSource.paginator = this.paginator;
        });
      },
      error: err => {
        console.error('Error cargando movimientos', err);
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

  exportExcel(): void {
    this.movementService.exportExcel();
  }

  exportByDriver(): void {
    const driverId = prompt('Ingrese ID del chofer');
    if (driverId) {
      this.movementService.exportExcelByDriver(Number(driverId));
    }
  }

  deleteMovement(id: number): void {
    if (confirm('¿Desea eliminar este movimiento?')) {
      this.movementService.deleteMovement(id).subscribe({
        next: () => this.loadMovements(),
        error: err => console.error('Error eliminando movimiento', err)
      });
    }
  }
}
