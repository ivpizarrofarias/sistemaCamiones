import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { PortService } from '../../core/services/entities.service';
import { Port } from '../../core/models/entities.model';

@Component({
  selector: 'app-port-create',
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
          <mat-card-title>Crear Destino</mat-card-title>
        </mat-card-header>

        <mat-card-content>
          <form [formGroup]="form" (ngSubmit)="save()">

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Nombre del Destino</mat-label>
              <input matInput formControlName="portName">
              <mat-error *ngIf="form.controls.portName.invalid">
                El nombre del destino es obligatorio
              </mat-error>
            </mat-form-field>

            <div class="actions">
              <button mat-button routerLink="/ports">Cancelar</button>
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
class PortCreateComponent {

  private fb = inject(FormBuilder);
  private portService = inject(PortService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);

  form = this.fb.nonNullable.group({
    portName: ['', Validators.required]
  });

  private capitalizeWords(value: string): string {
    return value
      .trim()
      .split(/\s+/)
      .map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
      .join(' ');
  }

  save(): void {
    if (this.form.invalid) return;

    const payload: Port = {
      portName: this.capitalizeWords(this.form.value.portName!)
    };

    console.log('Payload enviado al backend:', payload);

    this.portService.createPort(payload).subscribe({
      next: () => {
        this.snackBar.open('Destino creado correctamente', 'Cerrar', {
          duration: 3000
        });
        this.router.navigate(['/ports']);
      },
      error: err => {
        console.error('Error backend:', err);
        this.snackBar.open('Error al crear destino', 'Cerrar', {
          duration: 3000
        });
      }
    });
  }
}

export default PortCreateComponent;
