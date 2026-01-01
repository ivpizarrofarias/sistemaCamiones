import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { ClientService } from '../../core/services/entities.service';
import { Client } from '../../core/models/entities.model';

@Component({
  selector: 'app-client-create',
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
          <mat-card-title>Crear Cliente</mat-card-title>
        </mat-card-header>

        <mat-card-content>
          <form [formGroup]="form" (ngSubmit)="save()">

            <!-- Nombre -->
            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Nombre</mat-label>
              <input matInput formControlName="clientName">
              <mat-error *ngIf="form.controls.clientName.invalid">
                El nombre es obligatorio
              </mat-error>
            </mat-form-field>

            <!-- Email -->
            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Email</mat-label>
              <input matInput formControlName="clientEmail">
              <mat-error *ngIf="form.controls.clientEmail.invalid">
                Ingrese un email válido
              </mat-error>
            </mat-form-field>


            <div class="actions">
              <button mat-button routerLink="/clients">Cancelar</button>
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
export default class ClientCreateComponent {

  private fb = inject(FormBuilder);
  private clientService = inject(ClientService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);

  form = this.fb.nonNullable.group({
    clientName: ['', Validators.required],
    clientEmail: ['', [Validators.required, Validators.email]],
    clientPhone: ['', Validators.required] // se mantiene tal cual
  });

  // Función para capitalizar la primera letra de cada palabra
  private capitalizeWords(value: string): string {
    return value
      .trim()
      .split(/\s+/)
      .map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
      .join(' ');
  }

  save(): void {
    if (this.form.invalid) return;

    const payload: Client = {
      clientName: this.capitalizeWords(this.form.value.clientName!), // aplica capitalización
      clientEmail: this.form.value.clientEmail!,

    };

    console.log('Payload enviado al backend:', payload);

    this.clientService.createClient(payload).subscribe({
      next: () => {
        this.snackBar.open('Cliente creado correctamente', 'Cerrar', {
          duration: 3000
        });
        this.router.navigate(['/clients']);
      },
      error: err => {
        console.error('Error backend:', err);
        this.snackBar.open('Error al crear cliente', 'Cerrar', {
          duration: 3000
        });
      }
    });
  }

}
