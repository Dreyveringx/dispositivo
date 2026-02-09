import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterLink, RouterOutlet],
  template: `
    <header class="page-header">
      <div class="container">
        <nav class="d-flex justify-content-between align-items-center">
          <a routerLink="/" class="text-white text-decoration-none fw-bold fs-4">Dispositivos Inteligentes</a>
          <div>
            <a routerLink="/" class="btn btn-outline-light btn-sm me-2">Inicio</a>
            <a routerLink="/admin" class="btn btn-light btn-sm">Administración</a>
          </div>
        </nav>
      </div>
    </header>
    <main class="container mb-5">
      <router-outlet></router-outlet>
    </main>
    <footer class="text-center text-muted py-3 small border-top">
      Proyecto académico - Arquitectura Hexagonal y Microservicios
    </footer>
  `,
})
export class AppComponent {}
