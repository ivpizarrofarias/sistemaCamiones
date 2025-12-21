import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [CommonModule],
  template: `
    <footer class="footer">
      <div class="container">
        <span>&copy; 2025 Sistema de Gestión de Camiones. Todos los derechos reservados.</span>
      </div>
    </footer>
  `,
  styles: [`
    .footer {
      background-color: #f8f9fa;
      border-top: 1px solid #dee2e6;
      padding: 1rem 0;
      text-align: center;
      position: fixed;
      bottom: 0;
      left: 250px;
      right: 0;
      z-index: 1000;
    }
    .container {
      padding-left: 15px;
      padding-right: 15px;
    }
  `]
})
export class FooterComponent {}
