import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  AbstractControl,
  FormBuilder,
  ReactiveFormsModule,
  ValidationErrors,
  Validators,
  ɵElement,
  ɵValue
} from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { UserService } from '../../core/services/user.service';

type UserRole = 'ADMINISTRADOR' | 'SUPERADMINISTRADOR';

@Component({
  selector: 'app-user-create',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatSnackBarModule
  ],
  template: `
    <div class="page-container">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Crear Usuario</mat-card-title>
        </mat-card-header>

        <mat-card-content>
          <form [formGroup]="form" (ngSubmit)="save()">

            <div class="row">
              <mat-form-field appearance="outline">
                <mat-label>Nombre</mat-label>
                <input matInput formControlName="firstName">
              </mat-form-field>

              <mat-form-field appearance="outline">
                <mat-label>Apellido Paterno</mat-label>
                <input matInput formControlName="paternalLastName">
              </mat-form-field>

              <mat-form-field appearance="outline">
                <mat-label>Apellido Materno</mat-label>
                <input matInput formControlName="maternalLastName">
              </mat-form-field>
            </div>

            <div class="row">
              <mat-form-field appearance="outline">
                <mat-label>Email</mat-label>
                <input matInput type="email" formControlName="email">
              </mat-form-field>

              <mat-form-field appearance="outline">
                <mat-label>Password</mat-label>
                <input matInput type="password" formControlName="password">
              </mat-form-field>

              <mat-form-field appearance="outline">
                <mat-label>Rol</mat-label>
                <mat-select formControlName="userRole">
                  <mat-option value="ADMINISTRADOR">Administrador</mat-option>
                  <mat-option value="SUPERADMINISTRADOR">Super Administrador</mat-option>
                </mat-select>
              </mat-form-field>
            </div>

            <div class="actions">
              <button mat-button routerLink="/users">Cancelar</button>
              <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid">
                Guardar
              </button>
            </div>

          </form>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .page-container {
      padding: 20px;
      max-width: 900px;
      margin: auto;
    }

    .row {
      display: flex;
      gap: 24px;
      flex-wrap: wrap;
      margin-bottom: 24px;
    }

    mat-form-field {
      flex: 1;
      min-width: 220px;
    }

    .actions {
      display: flex;
      justify-content: flex-end;
      gap: 12px;
      margin-top: 16px;
    }
  `]
})
class UserCreateComponent {

  private fb = inject(FormBuilder);
  private userService = inject(UserService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);

  form = this.fb.nonNullable.group({
    firstName: ['', Validators.required],
    paternalLastName: ['', Validators.required],
    maternalLastName: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required],
    userRole: 'ADMINISTRADOR' as UserRole
  });

  private capitalize(value: ɵValue<ɵElement<(string | ((control: AbstractControl) => (ValidationErrors | null)))[], never>> | undefined): string {
    // @ts-ignore
    return value.charAt(0).toUpperCase() + value.slice(1).toLowerCase();
  }

  save(): void {
    if (this.form.invalid) return;

    const payload = {
      ...this.form.getRawValue(),
      firstName: this.capitalize(this.form.value.firstName),
      paternalLastName: this.capitalize(this.form.value.paternalLastName),
      maternalLastName: this.capitalize(this.form.value.maternalLastName)
    };

    this.userService.createUser(payload).subscribe({
      next: () => {
        this.snackBar.open('Usuario creado correctamente', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/users']);
      },
      error: () => {
        this.snackBar.open('Error al crear usuario', 'Cerrar', { duration: 3000 });
      }
    });
  }
}

export default UserCreateComponent
