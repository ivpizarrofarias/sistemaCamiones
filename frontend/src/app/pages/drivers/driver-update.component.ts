import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { DriverService } from '../../core/services/driver.service';
import { Driver } from '../../core/models/driver.model';

@Component({
  selector: 'app-driver-edit',
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
          <mat-card-title>Editar Conductor</mat-card-title>
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
                <input matInput formControlName="driverRut" readonly>
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
export class DriverEditComponent implements OnInit {

  private fb = inject(FormBuilder);
  private driverService = inject(DriverService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  form = this.fb.nonNullable.group({
    driverName: ['', Validators.required],
    driverRut: [{ value: '', disabled: true }, Validators.required],
    driverLicenseNumber: [{ value: '', disabled: true }, Validators.required]
  });

  driverId!: number;

  ngOnInit(): void {
    // Obtener el ID del conductor desde la ruta
    this.driverId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadDriver();
  }

  loadDriver(): void {
    this.driverService.getDriverById(this.driverId).subscribe({
      next: (response) => {
        if (response?.data) {
          const driver = response.data;
          this.form.patchValue({
            driverName: driver.driverName,
            driverRut: driver.driverRut,
            driverLicenseNumber: driver.driverLicenseNumber
          });
        } else {
          this.snackBar.open('Conductor no encontrado', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/drivers']);
        }
      },
      error: (err) => {
        console.error('Error al cargar conductor', err);
        this.snackBar.open('Error al cargar conductor', 'Cerrar', { duration: 3000 });
      }
    });
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const raw = this.form.getRawValue();
    const payload: Driver = {
      driverName: raw.driverName.trim().toUpperCase(),
      driverRut: raw.driverRut,
      driverLicenseNumber: raw.driverLicenseNumber
    };

    this.driverService.updateDriver(this.driverId, payload).subscribe({
      next: () => {
        this.snackBar.open('Conductor actualizado correctamente', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/drivers']);
      },
      error: (err) => {
        console.error('Error al actualizar conductor', err);
        this.snackBar.open('Error al actualizar conductor', 'Cerrar', { duration: 3000 });
      }
    });
  }
}
