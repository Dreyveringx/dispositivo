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
  templateUrl: './home.component.html',
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
