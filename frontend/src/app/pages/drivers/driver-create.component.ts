import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { DriverService } from '../../core/services/driver.service';
import { Driver } from '../../core/models/driver.model';

@Component({
  selector: 'app-driver-create',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSnackBarModule
  ],
  template: `
    <div class="page-container">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Crear Conductor</mat-card-title>
        </mat-card-header>

        <mat-card-content>
          <form [formGroup]="form" (ngSubmit)="save()">

            <div class="row">
              <mat-form-field appearance="outline">
                <mat-label>Nombre</mat-label>
                <input matInput formControlName="driverName">
                <mat-error *ngIf="form.controls.driverName.hasError('required')">
                  El nombre es obligatorio
                </mat-error>
              </mat-form-field>

              <mat-form-field appearance="outline">
                <mat-label>RUT</mat-label>
                <input
                  matInput
                  formControlName="driverRut"
                  placeholder="16.776.003-K"
                  maxlength="12"
                >
                <mat-error *ngIf="form.controls.driverRut.hasError('required')">
                  El RUT es obligatorio
                </mat-error>
                <mat-error *ngIf="form.controls.driverRut.hasError('rutExist')">
                  Este RUT ya está registrado
                </mat-error>
                <mat-error *ngIf="form.controls.driverRut.hasError('pattern')">
                  RUT inválido (Ej: 16.776.003-K)
                </mat-error>
              </mat-form-field>
            </div>

            <div class="row">
              <mat-form-field appearance="outline">
                <mat-label>N° Licencia</mat-label>
                <input matInput formControlName="driverLicenseNumber" readonly>
              </mat-form-field>
            </div>

            <div class="actions">
              <button mat-button routerLink="/drivers">Cancelar</button>
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
    .page-container { padding: 20px; max-width: 800px; margin: auto; }
    .row { display: flex; gap: 24px; flex-wrap: wrap; margin-bottom: 24px; }
    mat-form-field { flex: 1; min-width: 220px; }
    .actions { display: flex; justify-content: flex-end; gap: 12px; margin-top: 16px; }
  `]
})
export class DriverCreateComponent implements OnInit {
  private fb = inject(FormBuilder);
  private driverService = inject(DriverService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);

  form = this.fb.nonNullable.group({
    driverName: ['', Validators.required],
    driverRut: ['', [
      Validators.required,
      Validators.pattern(/^\d{1,2}\.\d{3}\.\d{3}-[0-9Kk]$/)
    ]],
    driverLicenseNumber: [{ value: '', disabled: true }, Validators.required]
  });

  ngOnInit(): void {
    // Formatea RUT en tiempo real y completa la licencia
    this.form.controls.driverRut.valueChanges.subscribe(value => {
      const formattedRut = this.autoFormatRut(value || '');
      this.form.controls.driverRut.setValue(formattedRut, { emitEvent: false });
      this.form.controls.driverLicenseNumber.setValue(formattedRut, { emitEvent: false });
    });
  }

  // Capitaliza cada palabra de un string
  private capitalizeWords(value: string): string {
    return value
      .trim()
      .split(/\s+/)
      .map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
      .join(' ');
  }

  // Formatea RUT chileno con puntos y guion automáticamente
  private autoFormatRut(value: string): string {
    let rut = value.toUpperCase().replace(/[^0-9K]/g, '');
    if (rut.length === 0) return '';
    if (rut.length === 1) return rut;
    const dv = rut.slice(-1);
    let body = rut.slice(0, -1);
    body = body.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
    return `${body}-${dv}`;
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const raw = this.form.getRawValue();

    // Validar unicidad de RUT
    this.driverService.getAllDrivers().subscribe({
      next: resp => {
        const exists = resp.data.some(d => d.driverRut === raw.driverRut);
        if (exists) {
          this.form.controls.driverRut.setErrors({ rutExist: true });
          return;
        }

        const payload: Driver = {
          driverName: this.capitalizeWords(raw.driverName), // <- Capitalización aplicada
          driverRut: raw.driverRut,
          driverLicenseNumber: raw.driverLicenseNumber
        };

        this.driverService.createDriver(payload).subscribe({
          next: () => {
            this.snackBar.open('Conductor creado correctamente', 'Cerrar', { duration: 3000 });
            this.router.navigate(['/drivers']);
          },
          error: err => {
            console.error('Error al crear conductor', err);
            this.snackBar.open('Error al crear conductor', 'Cerrar', { duration: 3000 });
          }
        });
      },
      error: err => {
        console.error('Error validando RUT', err);
        this.snackBar.open('Error al validar RUT', 'Cerrar', { duration: 3000 });
      }
    });
  }
}
