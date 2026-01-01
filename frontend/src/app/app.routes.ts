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
        path:'drivers',
        loadComponent: () =>
          import('./pages/drivers/driver-list.component')
            .then(m => m.DriverListComponent)
      },
      { path:'drivers/create',
        loadComponent: () =>
          import('./pages/drivers/driver-create.component')
            .then(m => m.DriverCreateComponent)
      },
      { path:'drivers/edit/:id',
        loadComponent:()=>
          import('./pages/drivers/driver-update.component')
            .then(m=>m.DriverEditComponent)
      },
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      }
    ]
  }
];
export default routes
