import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { ContainerService } from '../../core/services/entities.service';
import { Container } from '../../core/models/entities.model';

@Component({
  selector: 'app-container-create',
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
          <mat-card-title>Crear Contenedor</mat-card-title>
        </mat-card-header>

        <mat-card-content>
          <form [formGroup]="form" (ngSubmit)="save()">

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Código del contenedor</mat-label>
              <input
                matInput
                formControlName="containerCode"
                (input)="formatContainerCode($event)"
                placeholder="BICU 123456 7"
                maxlength="13"
              >
              <mat-error *ngIf="form.controls.containerCode.invalid">
                Código inválido
              </mat-error>
            </mat-form-field>

            <div class="actions">
              <button mat-button routerLink="/containers">Cancelar</button>
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
    .full-width { width: 100%; }
    .actions {
      display: flex;
      justify-content: flex-end;
      gap: 12px;
      margin-top: 20px;
    }
  `]
})
export default class ContainerCreateComponent {

  private fb = inject(FormBuilder);
  private containerService = inject(ContainerService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);

  form = this.fb.nonNullable.group({
    containerCode: ['', [
      Validators.required,
      Validators.pattern(/^[A-Z]{4}\s\d{6}\s\d$/)
    ]]
  });

  /**
   * Formato automático: BICU 123456 7
   */
  formatContainerCode(event: Event): void {
    let value = (event.target as HTMLInputElement).value.toUpperCase();

    // Quitar todo lo que no sea letras o números
    value = value.replace(/[^A-Z0-9]/g, '');

    let formatted = '';

    if (value.length > 0) {
      formatted += value.substring(0, 4); // Letras
    }

    if (value.length > 4) {
      formatted += ' ' + value.substring(4, 10); // 6 números
    }

    if (value.length > 10) {
      formatted += ' ' + value.substring(10, 11); // Dígito final
    }

    this.form.controls.containerCode.setValue(formatted, { emitEvent: false });
  }

  save(): void {
    if (this.form.invalid) return;

    const payload: Container = {
      containerCode: this.form.value.containerCode!
    };

    console.log('Payload enviado:', payload);

    this.containerService.createContainer(payload).subscribe({
      next: () => {
        this.snackBar.open('Contenedor creado correctamente', 'Cerrar', {
          duration: 3000
        });
        this.router.navigate(['/containers']);
      },
      error: err => {
        console.error('Error backend:', err);
        this.snackBar.open('Error al crear contenedor', 'Cerrar', {
          duration: 3000
        });
      }
    });
  }
}
