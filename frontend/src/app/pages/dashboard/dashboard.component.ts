import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { TruckService } from '../../core/services/truck.service';
import { DriverService } from '../../core/services/driver.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, MatCardModule],
  template: `
    <div class="dashboard-container">
      <h1>Dashboard</h1>
      <div class="stats-grid">
        <mat-card class="stat-card">
          <mat-card-header>
            <mat-card-title>Camiones Activos</mat-card-title>
          </mat-card-header>
          <mat-card-content>
            <p class="stat-value">{{ truckCount }}</p>
          </mat-card-content>
        </mat-card>
        <mat-card class="stat-card">
          <mat-card-header>
            <mat-card-title>Conductores</mat-card-title>
          </mat-card-header>
          <mat-card-content>
            <p class="stat-value">{{ driverCount }}</p>
          </mat-card-content>
        </mat-card>
        <mat-card class="stat-card">
          <mat-card-header>
            <mat-card-title>Entregas Hoy</mat-card-title>
          </mat-card-header>
          <mat-card-content>
            <p class="stat-value">12</p>
          </mat-card-content>
        </mat-card>
      </div>
    </div>
  `,
  styles: [`
    .dashboard-container {
      padding: 20px;
    }
    .stats-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 20px;
      margin-top: 20px;
    }
    .stat-card {
      text-align: center;
    }
    .stat-value {
      font-size: 2.5rem;
      font-weight: bold;
      color: #3f51b5;
      margin: 20px 0;
    }
  `]
})
export class DashboardComponent implements OnInit {
  private truckService = inject(TruckService);
  private driverService = inject(DriverService);

  truckCount: number = 0;
  driverCount: number = 0;

  ngOnInit(): void {
    this.loadStats();
  }

  loadStats(): void {
    console.log('Cargando estadísticas del dashboard...');
    forkJoin({
      trucks: this.truckService.getAllTrucks(),
      drivers: this.driverService.getAllDrivers()
    }).subscribe({
      next: (result: any) => {
        console.log('Datos de estadísticas recibidos:', result);
        // Manejar tanto formato ApiResponse como directo
        const trucksData = result.trucks.data || result.trucks;
        const driversData = result.drivers.data || result.drivers;

        this.truckCount = result.trucks.count || (Array.isArray(trucksData) ? trucksData.length : 0);
        this.driverCount = result.drivers.count || (Array.isArray(driversData) ? driversData.length : 0);
        console.log(`Conteo final -> Camiones: ${this.truckCount}, Conductores: ${this.driverCount}`);
      },
      error: (err) => {
        console.error('Error loading stats', err);
      }
    });
  }
}
