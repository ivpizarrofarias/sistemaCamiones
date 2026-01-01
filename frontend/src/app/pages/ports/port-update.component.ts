import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule, ActivatedRoute } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { PortService } from '../../core/services/entities.service';
import { Port } from '../../core/models/entities.model';

@Component({
  selector: 'app-port-update',
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
          <mat-card-title>Editar Destino</mat-card-title>
        </mat-card-header>

        <mat-card-content>
          <form [formGroup]="form" (ngSubmit)="update()">

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
export default class PortUpdateComponent implements OnInit {

  private fb = inject(FormBuilder);
  private portService = inject(PortService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  private portId!: number;

  form = this.fb.nonNullable.group({
    portName: ['', Validators.required]
  });

  ngOnInit(): void {
    this.portId = Number(this.route.snapshot.paramMap.get('id'));

    if (!this.portId) {
      this.snackBar.open('ID de destino inválido', 'Cerrar', { duration: 3000 });
      this.router.navigate(['/ports']);
      return;
    }

    this.loadPort();
  }

  private loadPort(): void {
    this.portService.getPortById(this.portId).subscribe({
      next: (port: Port) => {
        this.form.patchValue({
          portName: port.portName
        });
      },
      error: err => {
        console.error('Error cargando destino:', err);
        this.snackBar.open('Error al cargar destino', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/ports']);
      }
    });
  }

  private capitalizeWords(value: string): string {
    return value
      .trim()
      .split(/\s+/)
      .map(w => w.charAt(0).toUpperCase() + w.slice(1).toLowerCase())
      .join(' ');
  }

  update(): void {
    if (this.form.invalid) return;

    const payload: Port = {
      portName: this.capitalizeWords(this.form.value.portName!)
    };

    console.log('Actualizando destino:', this.portId, payload);

    this.portService.updatePort(this.portId, payload).subscribe({
      next: () => {
        this.snackBar.open('Destino actualizado correctamente', 'Cerrar', {
          duration: 3000
        });
        this.router.navigate(['/ports']);
      },
      error: err => {
        console.error('Error backend:', err);
        this.snackBar.open('Error al actualizar destino', 'Cerrar', {
          duration: 3000
        });
      }
    });
  }
}
