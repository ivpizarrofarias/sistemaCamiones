import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { GroundTransportService } from '../../core/services/entities.service';
import { GroundTransport } from '../../core/models/entities.model';

@Component({
  selector: 'app-ground-transport-create',
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
          <mat-card-title>Crear Transporte Terrestre</mat-card-title>
        </mat-card-header>

        <mat-card-content>
          <form [formGroup]="form" (ngSubmit)="save()">

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Nombre del Transportista</mat-label>
              <input matInput formControlName="transporterName">

              <mat-error *ngIf="form.controls.transporterName.invalid">
                El nombre del transportista es obligatorio
              </mat-error>
            </mat-form-field>

            <div class="actions">
              <button mat-button routerLink="/ground-transports">
                Cancelar
              </button>

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
class GroundTransportCreateComponent {

  private fb = inject(FormBuilder);
  private transportService = inject(GroundTransportService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);

  form = this.fb.nonNullable.group({
    transporterName: ['', Validators.required]
  });

  /** ✅ Convierte a "Rampla Corta" */
  private capitalizeWords(value: string): string {
    return value
      .toLowerCase()
      .split(' ')
      .filter(word => word.trim().length > 0)
      .map(word => word.charAt(0).toUpperCase() + word.slice(1))
      .join(' ');
  }

  save(): void {
    if (this.form.invalid) return;

    const formattedName = this.capitalizeWords(
      this.form.value.transporterName!.trim()
    );

    const payload: GroundTransport = {
      transporterName: formattedName
    };

    console.log('Payload enviado:', payload);

    this.transportService.createGroundTransport(payload).subscribe({
      next: () => {
        this.snackBar.open(
          'Transporte creado correctamente',
          'Cerrar',
          { duration: 3000 }
        );
        this.router.navigate(['/ground-transports']);
      },
      error: err => {
        console.error('Error backend:', err);
        this.snackBar.open(
          'Error al crear transporte',
          'Cerrar',
          { duration: 3000 }
        );
      }
    });
  }
}

/* 👇 IMPORTANTE 👇 */
export default GroundTransportCreateComponent;
