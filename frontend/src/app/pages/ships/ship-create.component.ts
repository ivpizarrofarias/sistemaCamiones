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
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { ShipService } from '../../core/services/entities.service';
import { Ship } from '../../core/models/entities.model';

@Component({
  selector: 'app-ship-create',
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
          <mat-card-title>Crear Nave</mat-card-title>
        </mat-card-header>

        <mat-card-content>
          <form [formGroup]="form" (ngSubmit)="save()">

            <div class="row">
              <mat-form-field appearance="outline">
                <mat-label>Nombre de la Nave</mat-label>
                <input matInput formControlName="shipName">
              </mat-form-field>

              <mat-form-field appearance="outline">
                <mat-label>Nro de Viaje</mat-label>
                <input matInput formControlName="voyageNumber">
              </mat-form-field>

              <mat-form-field appearance="outline">
                <mat-label>Línea Naviera</mat-label>
                <input matInput formControlName="shippingLine">
              </mat-form-field>
            </div>

            <div class="actions">
              <button mat-button routerLink="/ships">Cancelar</button>
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
class ShipCreateComponent {

  private fb = inject(FormBuilder);
  private shipService = inject(ShipService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);

  form = this.fb.nonNullable.group({
    shipName: ['', Validators.required],
    voyageNumber: ['', Validators.required],
    shippingLine: ['', Validators.required]
  });

  private capitalize(
    value: ɵValue<
      ɵElement<
        (string | ((control: AbstractControl) => ValidationErrors | null))[],
        never
      >
    > | undefined
  ): string {
    // @ts-ignore
    return value.charAt(0).toUpperCase() + value.slice(1).toLowerCase();
  }

  save(): void {
    if (this.form.invalid) return;

    const payload: Ship = {
      ...this.form.getRawValue(),
      shipName: this.capitalize(this.form.value.shipName),
      shippingLine: this.capitalize(this.form.value.shippingLine)
    };

    this.shipService.createShip(payload).subscribe({
      next: () => {
        this.snackBar.open('Nave creada correctamente', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/ships']);
      },
      error: () => {
        this.snackBar.open('Error al crear nave', 'Cerrar', { duration: 3000 });
      }
    });
  }
}

export default ShipCreateComponent;
