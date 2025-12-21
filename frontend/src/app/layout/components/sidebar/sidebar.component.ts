import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, MatListModule, MatIconModule, RouterModule],
  template: `
    <div class="sidebar-content">
      <mat-nav-list>
        <a mat-list-item routerLink="/dashboard" routerLinkActive="active-link">
          <mat-icon matListItemIcon>dashboard</mat-icon>
          <div matListItemTitle>Dashboard</div>
        </a>
        <a mat-list-item routerLink="/ships" routerLinkActive="active-link">
          <mat-icon matListItemIcon>directions_boat</mat-icon>
          <div matListItemTitle>Naves</div>
        </a>
        <a mat-list-item routerLink="/trucks" routerLinkActive="active-link">
          <mat-icon matListItemIcon>local_shipping</mat-icon>
          <div matListItemTitle>Camiones</div>
        </a>
        <a mat-list-item routerLink="/drivers" routerLinkActive="active-link">
          <mat-icon matListItemIcon>person</mat-icon>
          <div matListItemTitle>Conductores</div>
        </a>

        <a mat-list-item routerLink="/trips" routerLinkActive="active-link">
          <mat-icon matListItemIcon>map</mat-icon>
          <div matListItemTitle>Origen</div>
        </a>

        <a mat-list-item routerLink="/ports" routerLinkActive="active-link">
          <mat-icon matListItemIcon>anchor</mat-icon>
          <div matListItemTitle>Destino</div>
        </a>
        <a mat-list-item routerLink="/containers" routerLinkActive="active-link">
          <mat-icon matListItemIcon>inventory_2</mat-icon>
          <div matListItemTitle>Contenedores</div>
        </a>
        <a mat-list-item routerLink="/clients" routerLinkActive="active-link">
          <mat-icon matListItemIcon>business</mat-icon>
          <div matListItemTitle>Clientes</div>
        </a>
        <a mat-list-item routerLink="/ground-transports" routerLinkActive="active-link">
          <mat-icon matListItemIcon>commute</mat-icon>
          <div matListItemTitle>Transportes</div>
        </a>
        <a mat-list-item routerLink="/movements" routerLinkActive="active-link">
          <mat-icon matListItemIcon>swap_horiz</mat-icon>
          <div matListItemTitle>Movimientos</div>
        </a>
        <a mat-list-item routerLink="/valuations" routerLinkActive="active-link">
          <mat-icon matListItemIcon>assessment</mat-icon>
          <div matListItemTitle>Valoraciones</div>
        </a>
        <a mat-list-item routerLink="/users" routerLinkActive="active-link">
          <mat-icon matListItemIcon>people</mat-icon>
          <div matListItemTitle>Usuarios</div>
        </a>
      </mat-nav-list>
    </div>
  `,
  styles: [`
    .sidebar-content {
      height: 100%;
    }
    .active-link {
      background-color: rgba(0, 0, 0, 0.04);
      color: #3f51b5;
    }
  `]
})
export class SidebarComponent {}
