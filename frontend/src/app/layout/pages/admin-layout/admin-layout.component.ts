import { Component, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { MatSidenav, MatSidenavModule } from '@angular/material/sidenav';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { map, shareReplay } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { SidebarComponent } from '../../components/sidebar/sidebar.component';
import { FooterComponent } from '../../components/footer/footer.component';

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    MatSidenavModule,
    NavbarComponent,
    SidebarComponent,
    FooterComponent
  ],
  template: `
    <div class="admin-layout">
      <app-navbar (toggleSidebar)="drawer.toggle()"></app-navbar>

      <mat-sidenav-container class="sidenav-container">
        <mat-sidenav #drawer class="sidenav"
            [attr.role]="(isHandset$ | async) ? 'dialog' : 'navigation'"
            [mode]="(isHandset$ | async) ? 'over' : 'side'"
            [opened]="(isHandset$ | async) === false">
          <app-sidebar></app-sidebar>
        </mat-sidenav>

        <mat-sidenav-content>
          <main class="content">
            <router-outlet></router-outlet>
          </main>
        </mat-sidenav-content>
      </mat-sidenav-container>

      <app-footer></app-footer>
    </div>
  `,
  styles: [`
    .admin-layout {
      display: flex;
      flex-direction: column;
      height: 100vh;
    }
    .sidenav-container {
      flex: 1;
      margin-top: 64px;
      margin-bottom: 57px;
    }
    .sidenav {
      width: 250px;
      background-color: #f8f9fa;
      border-right: 1px solid #dee2e6;
    }
    .content {
      padding: 20px;
      min-height: calc(100vh - 64px - 57px);
      background-color: #ffffff;
    }
    /* Estilos para asegurar que el sidenav se comporte bien en móvil */
    @media (max-width: 600px) {
      .sidenav-container {
        margin-top: 56px; /* Altura toolbar en móvil */
      }
    }
  `]
})
export class AdminLayoutComponent {
  private breakpointObserver = inject(BreakpointObserver);

  @ViewChild('drawer') drawer!: MatSidenav;

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );
}
