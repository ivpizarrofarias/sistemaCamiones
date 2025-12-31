import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { TruckService } from '../../core/services/truck.service';

@Component({
  selector: 'app-truck-create',
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
          <mat-card-title>Crear Camión</mat-card-title>
        </mat-card-header>

        <mat-card-content>
          <form [formGroup]="form" (ngSubmit)="save()">

            <div class="row">
              <mat-form-field appearance="outline">
                <mat-label>Patente</mat-label>
                <input
                  matInput
                  formControlName="licensePlate"
                  placeholder="Ej: CVTV33"
                >
                <mat-error *ngIf="form.controls.licensePlate.hasError('required')">
                  La patente es obligatoria
                </mat-error>
                <mat-error *ngIf="form.controls.licensePlate.hasError('pattern')">
                  Formato inválido (Ej: CVTV33)
                </mat-error>
              </mat-form-field>
            </div>

            <div class="actions">
              <button mat-button routerLink="/trucks">Cancelar</button>
              <button
                mat-raised-button
                color="primary"
                type="submit"
                [disabled]="form.invalid">
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
      max-width: 600px;
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
export default class TruckCreateComponent {

  private fb = inject(FormBuilder);
  private truckService = inject(TruckService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);

  form = this.fb.nonNullable.group({
    licensePlate: [
      '',
      [
        Validators.required,
        Validators.pattern(/^[a-zA-Z]{4}\d{2}$/)
      ]
    ]
  });

  /**
   * Convierte:
   * cvtv33 -> CV*TV*33
   */
  private formatLicensePlate(value: string): string {
    const clean = value.replace(/[^a-zA-Z0-9]/g, '').toUpperCase();
    return `${clean.substring(0, 2)}*${clean.substring(2, 4)}*${clean.substring(4, 6)}`;
  }

  save(): void {
    if (this.form.invalid) return;

    const rawLicensePlate = this.form.value.licensePlate!;

    const payload = {
      licensePlate: this.formatLicensePlate(rawLicensePlate)
    };

    this.truckService.createTruck(payload).subscribe({
      next: () => {
        this.snackBar.open('Camión creado correctamente', 'Cerrar', {
          duration: 3000
        });
        this.router.navigate(['/trucks']);
      },
      error: (err) => {
        console.error(err);
        this.snackBar.open(
          err?.error?.message ?? 'Error al crear camión',
          'Cerrar',
          { duration: 4000 }
        );
      }
    });
  }
}
