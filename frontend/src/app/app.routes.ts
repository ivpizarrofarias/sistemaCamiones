import { Routes } from '@angular/router';
import { AdminLayoutComponent } from './layout/pages/admin-layout/admin-layout.component';

export const routes: Routes = [
  {
    path: '',
    component: AdminLayoutComponent,
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./pages/dashboard/dashboard.component').then(m => m.DashboardComponent)
      },
      {
        path: 'trucks',
        loadComponent: () => import('./pages/trucks/truck-list.component').then(m => m.TruckListComponent)
      },
      {
        path: 'drivers',
        loadComponent: () => import('./pages/drivers/driver-list.component').then(m => m.DriverListComponent)
      },
      {
        path: 'valuations',
        loadComponent: () => import('./pages/valuations/valuation-list.component').then(m => m.ValuationListComponent)
      },
      {
        path: 'users',
        loadComponent: () => import('./pages/users/user-list.component').then(m => m.UserListComponent)
      },
      {
        path: 'trips',
        loadComponent: () => import('./pages/trips/trip-list.component').then(m => m.TripListComponent)
      },
      {
        path: 'ships',
        loadComponent: () => import('./pages/ships/ship-list.component').then(m => m.ShipListComponent)
      },
      {
        path: 'ports',
        loadComponent: () => import('./pages/ports/port-list.component').then(m => m.PortListComponent)
      },
      {
        path: 'movements',
        loadComponent: () => import('./pages/movements/movement-list.component').then(m => m.MovementListComponent)
      },
      {
        path: 'ground-transports',
        loadComponent: () => import('./pages/ground-transports/ground-transport-list.component').then(m => m.GroundTransportListComponent)
      },
      {
        path: 'containers',
        loadComponent: () => import('./pages/containers/container-list.component').then(m => m.ContainerListComponent)
      },
      {
        path: 'clients',
        loadComponent: () => import('./pages/clients/client-list.component').then(m => m.ClientListComponent)
      },
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      }
    ]
  }
];
