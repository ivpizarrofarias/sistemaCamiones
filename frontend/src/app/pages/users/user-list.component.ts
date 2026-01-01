import { Component, OnInit, inject, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule, MatPaginatorIntl } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { UserService } from '../../core/services/entities.service';
import { User } from '../../core/models/entities.model';
import { RouterLink } from '@angular/router';

// Configuración del paginador en español
export class SpanishPaginatorIntl extends MatPaginatorIntl {
  override itemsPerPageLabel = 'Elementos por página:';
  override nextPageLabel     = 'Siguiente página';
  override previousPageLabel = 'Página anterior';
  override firstPageLabel    = 'Primera página';
  override lastPageLabel     = 'Última página';
}

@Component({
  selector: 'app-user-list',
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
        <mat-card-header class="header">
          <mat-card-title>Usuarios</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <!-- Botón Crear -->
          <div class="header-actions">
            <button mat-raised-button color="primary" routerLink="/users/create">
              <mat-icon>person_add</mat-icon>
              Crear Usuario
            </button>
          </div>

          <!-- Espaciador -->
          <div class="spacer"></div>

          <!-- Buscador -->
          <mat-form-field appearance="outline" class="search-field">
            <mat-label>Buscar usuario</mat-label>
            <input matInput (keyup)="applyFilter($event)" placeholder="Nombre, Email o Rol" #input>
            <mat-icon matSuffix>search</mat-icon>
          </mat-form-field>

          <!-- Tabla -->
          <table mat-table [dataSource]="dataSource" class="mat-elevation-z8">

            <ng-container matColumnDef="userId">
              <th mat-header-cell *matHeaderCellDef> ID </th>
              <td mat-cell *matCellDef="let user"> {{user.userId}} </td>
            </ng-container>

            <ng-container matColumnDef="fullName">
              <th mat-header-cell *matHeaderCellDef> Nombre Completo </th>
              <td mat-cell *matCellDef="let user"> {{user.firstName}} {{user.paternalLastName}} {{user.maternalLastName}} </td>
            </ng-container>

            <ng-container matColumnDef="email">
              <th mat-header-cell *matHeaderCellDef> Email </th>
              <td mat-cell *matCellDef="let user"> {{user.email}} </td>
            </ng-container>

            <ng-container matColumnDef="userRole">
              <th mat-header-cell *matHeaderCellDef> Rol </th>
              <td mat-cell *matCellDef="let user"> {{user.userRole}} </td>
            </ng-container>

            <ng-container matColumnDef="actions">
              <th mat-header-cell *matHeaderCellDef> Acciones </th>
              <td mat-cell *matCellDef="let user">
                <button mat-icon-button color="primary" [routerLink]="['/users/edit', user.userId]">
                  <mat-icon>edit</mat-icon>
                </button>
                <button mat-icon-button color="warn" (click)="deleteUser(user.userId)">
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
                  <div class="no-data">No hay usuarios registrados.</div>
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
export class UserListComponent implements OnInit {
  private userService = inject(UserService);
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  dataSource = new MatTableDataSource<User>([]);
  displayedColumns: string[] = ['userId', 'fullName', 'email', 'userRole', 'actions'];

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getAllUsers().subscribe({
      next: (response: any) => {
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
        console.error('Error al obtener usuarios:', err);
        this.dataSource.data = [];
      }
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator) this.dataSource.paginator.firstPage();
  }

  deleteUser(id: number): void {
    if (confirm('¿Está seguro de eliminar este usuario?')) {
      this.userService.deleteUser(id).subscribe({
        next: () => this.loadUsers(),
        error: (err) => console.error('Error eliminando usuario', err)
      });
    }
  }
}
