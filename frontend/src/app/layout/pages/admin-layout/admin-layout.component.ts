import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import {NavbarComponent} from "../../components/navbar/navbar.component";
import {SidebarComponent} from "../../components/sidebar/sidebar.component";
import{FooterComponent} from "../../components/footer/footer.component";

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    NavbarComponent,
    SidebarComponent,
    FooterComponent
  ],
  template: `
    <div class="layout">
      <app-navbar></app-navbar>

      <div class="content">
        <app-sidebar></app-sidebar>

        <main class="main-content">
          <router-outlet></router-outlet>
        </main>
      </div>

      <app-footer></app-footer>
    </div>
  `,
  styles: [`
    .layout {
      display: flex;
      flex-direction: column;
      height: 100vh;
    }

    .content {
      display: flex;
      flex: 1;
      margin-top: 64px;
    }

    app-sidebar {
      width: 260px;
    }

    .main-content {
      flex: 1;
      padding: 16px;
    }
  `]
})
export class AdminLayoutComponent {}
