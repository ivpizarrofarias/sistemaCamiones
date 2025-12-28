import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { UserService } from '../../core/services/entities.service';
import { User } from '../../core/models/entities.model';
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-user-list',
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
          <mat-card-title>Usuarios</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <div class="header-actions">
            <button mat-raised-button color="primary" routerLink="/users/create">
              <mat-icon>person_add</mat-icon>
              Crear Usuario
              </button>
          </div>

          <!-- Espaciador entre botón y buscador -->
          <div class="spacer"></div>

          <mat-form-field appearance="outline" class="search-field">
            <mat-label>Buscar usuario</mat-label>
            <input matInput (keyup)="applyFilter($event)" placeholder="Nombre, Email o Rol" #input>
            <mat-icon matSuffix>search</mat-icon>
          </mat-form-field>

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
                <button
                  mat-icon-button
                  color="primary"
                  [routerLink]="['/users/edit', user.userId]"
                >
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
      justify-content: flex-start; /* Cambiado de flex-end a flex-start */
      margin-bottom: 8px;
    }

    .spacer {
      height: 32px; /* Espacio entre el botón y el buscador */
      width: 100%;
    }
  `]
})
export class UserListComponent implements OnInit {
  private userService = inject(UserService);
  dataSource = new MatTableDataSource<User>([]);
  displayedColumns: string[] = ['userId', 'fullName', 'email', 'userRole', 'actions'];

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    console.log('UserListComponent: Iniciando carga de usuarios...');
    this.userService.getAllUsers().subscribe({
      next: (response) => {
        console.log('UserListComponent: Respuesta recibida:', response);

        if (response && response.success && Array.isArray(response.data)) {
          this.dataSource.data = response.data;
          console.log('UserListComponent: %d usuarios cargados correctamente', response.data.length);
        } else if (Array.isArray(response)) {
          this.dataSource.data = response;
          console.log('UserListComponent: %d usuarios cargados (formato array directo)', response.length);
        } else if (response && response.data && Array.isArray(response.data)) {
          this.dataSource.data = response.data;
          console.log('UserListComponent: %d usuarios cargados (formato .data)', response.data.length);
        } else {
          console.warn('UserListComponent: No se pudo determinar el formato de los datos:', response);
          this.dataSource.data = [];
        }
      },
      error: (err) => {
        console.error('UserListComponent: Error crítico al obtener usuarios:', err);
        this.dataSource.data = [];
      }
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  deleteUser(id: number): void {
    if (confirm('¿Está seguro de eliminar este usuario?')) {
      this.userService.deleteUser(id).subscribe({
        next: (response) => {
          if (response.success) {
            this.loadUsers();
          }
        },
        error: (err) => console.error('Error deleting user', err)
      });
    }
  }
}
