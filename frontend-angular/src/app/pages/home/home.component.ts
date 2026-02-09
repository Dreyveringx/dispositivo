import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DeviceService } from '../../services/device.service';
import { CatalogService } from '../../services/catalog.service';
import { Device } from '../../models/device.model';
import { Brand } from '../../models/brand.model';
import { DeviceType } from '../../models/device-type.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  template: `
    <h1 class="h3 mb-4">Catálogo de dispositivos</h1>

    <div class="card mb-4">
      <div class="card-body">
        <div class="row g-3">
          <div class="col-md-3">
            <label class="form-label">Buscar por nombre</label>
            <input type="text" class="form-control" placeholder="Nombre..." [(ngModel)]="searchName" (ngModelChange)="loadDevices()">
          </div>
          <div class="col-md-2">
            <label class="form-label">Marca</label>
            <select class="form-select" [(ngModel)]="filterBrandId" (ngModelChange)="loadDevices()">
              <option [ngValue]="null">Todas</option>
              <option *ngFor="let b of brands" [ngValue]="b.id">{{ b.name }}</option>
            </select>
          </div>
          <div class="col-md-2">
            <label class="form-label">Tipo</label>
            <select class="form-select" [(ngModel)]="filterTypeId" (ngModelChange)="loadDevices()">
              <option [ngValue]="null">Todos</option>
              <option *ngFor="let t of deviceTypes" [ngValue]="t.id">{{ t.name }}</option>
            </select>
          </div>
          <div class="col-md-2">
            <label class="form-label">Ordenar</label>
            <select class="form-select" [(ngModel)]="sortByReleaseDate" (ngModelChange)="loadDevices()">
              <option [ngValue]="true">Más recientes primero</option>
              <option [ngValue]="false">Más antiguos primero</option>
            </select>
          </div>
        </div>
      </div>
    </div>

    <div class="row g-4" *ngIf="!loading">
      <div class="col-sm-6 col-lg-4" *ngFor="let device of devices">
        <div class="card h-100">
          <img [src]="device.imageUrl || 'https://via.placeholder.com/300x180?text=Sin+imagen'" class="card-img-top" [alt]="device.name">
          <div class="card-body d-flex flex-column">
            <h5 class="card-title">{{ device.name }}</h5>
            <p class="card-text small text-muted" *ngIf="device.releaseDate">
              Lanzamiento: {{ device.releaseDate | date:'mediumDate' }}
            </p>
            <a [routerLink]="['/device', device.id]" class="btn btn-primary mt-auto">Ver detalle</a>
          </div>
        </div>
      </div>
    </div>
    <div *ngIf="loading" class="text-center py-5">
      <div class="spinner-border text-primary" role="status"></div>
      <p class="mt-2 text-muted">Cargando dispositivos...</p>
    </div>
    <div *ngIf="!loading && devices.length === 0" class="alert alert-info">
      No hay dispositivos que coincidan con los filtros.
    </div>
  `,
})
export class HomeComponent implements OnInit {
  devices: Device[] = [];
  brands: Brand[] = [];
  deviceTypes: DeviceType[] = [];
  searchName = '';
  filterBrandId: number | null = null;
  filterTypeId: number | null = null;
  sortByReleaseDate = true;
  loading = false;

  constructor(
    private deviceService: DeviceService,
    private catalogService: CatalogService,
  ) {}

  ngOnInit(): void {
    this.catalogService.getBrands().subscribe((b) => (this.brands = b));
    this.catalogService.getDeviceTypes().subscribe((t) => (this.deviceTypes = t));
    this.loadDevices();
  }

  loadDevices(): void {
    this.loading = true;
    this.deviceService.getList({
      name: this.searchName || undefined,
      brandId: this.filterBrandId ?? undefined,
      deviceTypeId: this.filterTypeId ?? undefined,
      sortByReleaseDate: this.sortByReleaseDate,
    }).subscribe({
      next: (list) => {
        this.devices = list;
        this.loading = false;
      },
      error: () => (this.loading = false),
    });
  }
}
