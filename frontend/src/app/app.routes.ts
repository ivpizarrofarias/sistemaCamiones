import { Routes } from '@angular/router';
import { AdminLayoutComponent } from './layout/pages/admin-layout/admin-layout.component';

export const routes: Routes = [
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
            .then(m => m.default) // 👈 sigue siendo default
      },
      {
        path: 'users/edit/:id',
        loadComponent: () =>
          import('./pages/users/user-update.component')
            .then(m => m.UserUpdateComponent) // ✅ CORRECTO
      },
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      }
    ]
  }
];
