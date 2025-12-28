import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators, FormControl } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import{User} from "../../core/models/entities.model";
import { UserService } from '../../core/services/user.service';

// type UserRole = 'ADMINISTRADOR' | 'SUPERADMINISTRADOR';

@Component({
  selector: 'app-user-update',
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
          <mat-card-title>Editar Usuario</mat-card-title>
        </mat-card-header>

        <mat-card-content>
          <form [formGroup]="form" (ngSubmit)="update()">

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
                <mat-label>Nueva Contraseña</mat-label>
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
                Actualizar
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
export class UserUpdateComponent implements OnInit {

  private fb = inject(FormBuilder);
  private userService = inject(UserService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  private userId!: number;
  form = this.fb.group({
    firstName: new FormControl('', { nonNullable: true, validators: Validators.required }),
    paternalLastName: new FormControl('', { nonNullable: true, validators: Validators.required }),
    maternalLastName: new FormControl('', { nonNullable: true, validators: Validators.required }),
    email: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.email] }),
    password: new FormControl('', Validators.minLength(6)), // 👈 AQUÍ
    userRole: new FormControl<string>({ value: 'ADMINISTRADOR', disabled: true })
  });



  ngOnInit(): void {
    this.userId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadUser();
  }

  private loadUser(): void {
    this.userService.getUserById(this.userId).subscribe({
      next: (response) => {
        const user = response.data;

        this.form.patchValue({
          firstName: user.firstName,
          paternalLastName: user.paternalLastName,
          maternalLastName: user.maternalLastName,
          email: user.email,
          userRole: user.userRole ?? 'ADMINISTRADOR'
        });
      },
      error: () => {
        this.snackBar.open('Error al cargar usuario', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/users']);
      }
    });
  }




  private capitalize(value?: string): string {
    if (!value) return '';
    return value.charAt(0).toUpperCase() + value.slice(1).toLowerCase();
  }

  update(): void {
    if (this.form.invalid) return;

    const payload: Partial<User> = {
      firstName: this.capitalize(this.form.controls.firstName.value),
      paternalLastName: this.capitalize(this.form.controls.paternalLastName.value),
      maternalLastName: this.capitalize(this.form.controls.maternalLastName.value),
      email: this.form.controls.email.value,
      userRole: this.form.controls.userRole.value ?? 'ADMINISTRADOR'
    };

    // 🔐 contraseña opcional
    if (this.form.controls.password.value?.trim()) {
      payload.password = this.form.controls.password.value;
    }

    console.log('Payload enviado:', payload);

    this.userService.updateUser(this.userId, payload).subscribe({
      next: () => {
        this.snackBar.open('Usuario actualizado correctamente', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/users']);
      },
      error: (err) => {
        console.error('Error al actualizar usuario', err);
        this.snackBar.open('Error al actualizar usuario', 'Cerrar', { duration: 3000 });
      }
    });
  }



}
