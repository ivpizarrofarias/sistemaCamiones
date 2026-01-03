import { Routes } from '@angular/router';
import { AdminLayoutComponent } from './layout/pages/admin-layout/admin-layout.component';

const routes: Routes = [
  {
    path: '',
    component: AdminLayoutComponent,
    children: [

      {
        path: 'dashboard',
        loadComponent: () =>
          import('./pages/dashboard/dashboard.component')
            .then(m => m.DashboardComponent)
      },

      {
        path: 'users',
        loadComponent: () =>
          import('./pages/users/user-list.component')
            .then(m => m.UserListComponent)
      },
      {
        path: 'users/create',
        loadComponent: () =>
          import('./pages/users/user-create.component')
            .then(m => m.UserCreateComponent)
      },
      {
        path: 'users/edit/:id',
        loadComponent: () =>
          import('./pages/users/user-update.component')
            .then(m => m.UserUpdateComponent)
      },

      {
        path: 'ships',
        loadComponent: () =>
          import('./pages/ships/ship-list.component')
            .then(m => m.ShipListComponent)
      },
      {
        path: 'ships/create',
        loadComponent: () =>
          import('./pages/ships/ship-create.component')
            .then(m => m.default)
      },
      {
        path: 'ships/edit/:id',
        loadComponent: () =>
          import('./pages/ships/ship-update.component')
            .then(m => m.default)
      },

      {
        path: 'trucks',
        loadComponent: () =>
          import('./pages/trucks/truck-list.component')
            .then(m => m.TruckListComponent)
      },
      {
        path: 'trucks/create',
        loadComponent: () =>
          import('./pages/trucks/truck-create.component')
            .then(m => m.default)
      },

      {
        path: 'drivers',
        loadComponent: () =>
          import('./pages/drivers/driver-list.component')
            .then(m => m.DriverListComponent)
      },
      {
        path: 'drivers/create',
        loadComponent: () =>
          import('./pages/drivers/driver-create.component')
            .then(m => m.DriverCreateComponent)
      },
      {
        path: 'drivers/edit/:id',
        loadComponent: () =>
          import('./pages/drivers/driver-update.component')
            .then(m => m.DriverEditComponent)
      },

      {
        path: 'trips',
        loadComponent: () =>
          import('./pages/trips/trip-list.component')
            .then(m => m.TripListComponent)
      },
      {
        path: 'trips/create',
        loadComponent: () =>
          import('./pages/trips/trip-create.component')
            .then(m => m.TripCreateComponent)
      },
      {
        path: 'trips/edit/:id',
        loadComponent: () =>
          import('./pages/trips/trip-update.component')
            .then(m => m.TripUpdateComponent)
      },

      {
        path: 'ports',
        loadComponent: () =>
          import('./pages/ports/port-list.component')
            .then(m => m.PortListComponent)
      },
      {
        path: 'ports/create',
        loadComponent: () =>
          import('./pages/ports/port-create.component')
            .then(m => m.default)
      },
      {
        path: 'ports/edit/:id',
        loadComponent: () =>
          import('./pages/ports/port-update.component')
            .then(m => m.default)
      },
      {
        path: 'containers',
        loadComponent: () =>
          import('./pages/containers/container-list.component')
            .then(m => m.ContainerListComponent)
      },
      {
        path: 'containers/create',
        loadComponent: () =>
          import('./pages/containers/container-create.component')
            .then(m => m.default)
      },
      {
        path:'clients',
        loadComponent: () =>
          import('./pages/clients/client-list.component')
            .then(m => m.ClientListComponent)
      },
      {
        path:'clients/create',
        loadComponent:()=>
          import('./pages/clients/client-create.component')
            .then(m=>m.default)
      },
      {
        path:'clients/edit/:id',
        loadComponent:()=>
          import('./pages/clients/client-update.component')
            .then(m=>m.ClientUpdateComponent)
      },
      {
        path:'ground-transports',
        loadComponent:()=>
          import('./pages/ground-transports/ground-transport-list.component')
            .then(m=>m.GroundTransportListComponent)
      },
      {
        path: 'ground-transports/create',
        loadComponent: () =>
          import('./pages/ground-transports/ground-transport-create.component')
            .then(m => m.default)
      },
      {
        path: 'ground-transports/edit/:id',
        loadComponent: () =>
          import('./pages/ground-transports/ground-transport-update.component')
            .then(m => m.default)
      },

      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      }
    ]
  }
];

export default routes;
