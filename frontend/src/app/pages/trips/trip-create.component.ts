import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { TripService } from '../../core/services/entities.service';
import { Trip } from '../../core/models/entities.model';

@Component({
  selector: 'app-trip-create',
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
          <mat-card-title>Crear Origen</mat-card-title>
        </mat-card-header>

        <mat-card-content>
          <form [formGroup]="form" (ngSubmit)="save()">

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Origen</mat-label>
              <input matInput formControlName="origin" placeholder="Ej: valparaiso puerto">
              <mat-error *ngIf="form.controls.origin.hasError('required')">
                El origen es obligatorio
              </mat-error>
            </mat-form-field>

            <div class="actions">
              <button mat-button routerLink="/trips">Cancelar</button>
              <button
                mat-raised-button
                color="primary"
                type="submit"
                [disabled]="form.invalid"
              >
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

    .full-width {
      width: 100%;
    }

    .actions {
      display: flex;
      justify-content: flex-end;
      gap: 12px;
      margin-top: 20px;
    }
  `]
})
export class TripCreateComponent {

  private fb = inject(FormBuilder);
  private tripService = inject(TripService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);

  form = this.fb.nonNullable.group({
    origin: ['', Validators.required]
  });

  // Capitaliza cada palabra: "valparaiso puerto" → "Valparaiso Puerto"
  private capitalizeWords(value: string): string {
    return value
      .trim()
      .toLowerCase()
      .split(' ')
      .filter(word => word.length > 0)
      .map(word => word.charAt(0).toUpperCase() + word.slice(1))
      .join(' ');
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload: Trip = {
      origin: this.capitalizeWords(this.form.value.origin!)
    };

    console.log('Payload enviado:', payload); // 👈 útil para verificar

    this.tripService.createTrip(payload).subscribe({
      next: () => {
        this.snackBar.open('Origen creado correctamente', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/trips']);
      },
      error: err => {
        console.error('ERROR COMPLETO:', err);
        console.error('STATUS:', err.status);
        console.error('BODY:', err.error);

        this.snackBar.open(
          err?.error?.message || 'Error al crear origen',
          'Cerrar',
          { duration: 5000 }
        );
      }

    });
  }

}
