import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { TripService } from '../../core/services/entities.service';
import { Trip } from '../../core/models/entities.model';

@Component({
  selector: 'app-trip-update',
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
          <mat-card-title>Editar Origen</mat-card-title>
        </mat-card-header>

        <mat-card-content>
          <form [formGroup]="form" (ngSubmit)="update()">

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Origen</mat-label>
              <input matInput formControlName="origin">
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
export class TripUpdateComponent implements OnInit {

  private fb = inject(FormBuilder);
  private tripService = inject(TripService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  private tripId!: number;

  form = this.fb.nonNullable.group({
    origin: ['', Validators.required]
  });

  ngOnInit(): void {
    this.tripId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadTrip();
  }

  private loadTrip(): void {
    this.tripService.getTripById(this.tripId).subscribe({
      next: (trip: Trip) => {
        this.form.patchValue({
          origin: trip.origin
        });
      },
      error: err => {
        console.error('Error cargando origen', err);
        this.snackBar.open('Error al cargar origen', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/trips']);
      }
    });
  }

  // "valparaiso puerto" → "Valparaiso Puerto"
  private capitalizeWords(value: string): string {
    return value
      .trim()
      .toLowerCase()
      .split(' ')
      .filter(word => word.length > 0)
      .map(word => word.charAt(0).toUpperCase() + word.slice(1))
      .join(' ');
  }

  update(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload: Trip = {
      origin: this.capitalizeWords(this.form.value.origin!)
    };

    console.log('Payload UPDATE:', payload);

    this.tripService.updateTrip(this.tripId, payload).subscribe({
      next: () => {
        this.snackBar.open('Origen actualizado correctamente', 'Cerrar', {
          duration: 3000
        });
        this.router.navigate(['/trips']);
      },
      error: err => {
        console.error('ERROR UPDATE:', err);
        this.snackBar.open(
          err?.error?.message || 'Error al actualizar origen',
          'Cerrar',
          { duration: 5000 }
        );
      }
    });
  }
}
