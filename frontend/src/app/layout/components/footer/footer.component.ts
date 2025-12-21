import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [CommonModule, MatToolbarModule],
  template: `
    <mat-toolbar color="primary" class="footer">
      <span>© 2025 Sistema de Camiones</span>
    </mat-toolbar>
  `,
  styles: [`
    .footer {
      position: fixed;
      bottom: 0;
      left: 0;
      right: 0;
      height: 48px;
      justify-content: center;
      font-size: 14px;
    }
  `]
})
export class FooterComponent {}
