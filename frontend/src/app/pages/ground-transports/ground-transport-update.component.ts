import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule, ActivatedRoute } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { GroundTransportService } from '../../core/services/entities.service';
import { GroundTransport } from '../../core/models/entities.model';

@Component({
  selector: 'app-ground-transport-update',
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
          <mat-card-title>Editar Transporte Terrestre</mat-card-title>
        </mat-card-header>

        <mat-card-content>
          <form [formGroup]="form" (ngSubmit)="update()">

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
class GroundTransportUpdateComponent implements OnInit {

  private fb = inject(FormBuilder);
  private transportService = inject(GroundTransportService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  private transportId!: number;

  form = this.fb.nonNullable.group({
    transporterName: ['', Validators.required]
  });

  ngOnInit(): void {
    this.transportId = Number(this.route.snapshot.paramMap.get('id'));

    this.transportService.getGroundTransportById(this.transportId).subscribe({
      next: transport => {
        this.form.patchValue({
          transporterName: transport.transporterName
        });
      },
      error: () => {
        this.snackBar.open('Error al cargar transporte', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/ground-transports']);
      }
    });
  }

  update(): void {
    if (this.form.invalid) return;

    const payload: GroundTransport = {
      transporterName: this.capitalizeWords(
        this.form.value.transporterName!.trim()
      )
    };

    this.transportService.updateGroundTransport(this.transportId, payload).subscribe({
      next: () => {
        this.snackBar.open(
          'Transporte actualizado correctamente',
          'Cerrar',
          { duration: 3000 }
        );
        this.router.navigate(['/ground-transports']);
      },
      error: err => {
        console.error('Error backend:', err);
        this.snackBar.open(
          'Error al actualizar transporte',
          'Cerrar',
          { duration: 3000 }
        );
      }
    });
  }

  /** 👉 Convierte a "Rampla Corta" */
  private capitalizeWords(text: string): string {
    return text
      .toLowerCase()
      .split(' ')
      .filter(word => word.length > 0)
      .map(word => word[0].toUpperCase() + word.substring(1))
      .join(' ');
  }
}

/* 👇 IMPORTANTE 👇 */
export default GroundTransportUpdateComponent;
