import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, FormArray, FormControl, Validators } from '@angular/forms';
import { DeviceService } from '../../services/device.service';
import { CatalogService } from '../../services/catalog.service';
import { Device } from '../../models/device.model';
import { Brand } from '../../models/brand.model';
import { DeviceType } from '../../models/device-type.model';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  template: `
    <h1 class="h3 mb-4">Administración</h1>

    <ul class="nav nav-tabs mb-4">
      <li class="nav-item">
        <button class="nav-link" [class.active]="tab === 'devices'" (click)="tab = 'devices'">Dispositivos</button>
      </li>
      <li class="nav-item">
        <button class="nav-link" [class.active]="tab === 'brands'" (click)="tab = 'brands'">Marcas</button>
      </li>
      <li class="nav-item">
        <button class="nav-link" [class.active]="tab === 'types'" (click)="tab = 'types'">Tipos</button>
      </li>
    </ul>

    <!-- Dispositivos -->
    <div *ngIf="tab === 'devices'">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h5>CRUD Dispositivos</h5>
        <button class="btn btn-primary" (click)="openDeviceForm()">Nuevo dispositivo</button>
      </div>
      <div class="table-responsive">
        <table class="table table-hover">
          <thead>
            <tr><th>Id</th><th>Nombre</th><th>Marca</th><th>Tipo</th><th>Fecha</th><th></th></tr>
          </thead>
          <tbody>
            <tr *ngFor="let d of devices">
              <td>{{ d.id }}</td>
              <td>{{ d.name }}</td>
              <td>{{ getBrandName(d.brandId) }}</td>
              <td>{{ getTypeName(d.deviceTypeId) }}</td>
              <td>{{ d.releaseDate | date:'shortDate' }}</td>
              <td>
                <button class="btn btn-sm btn-outline-primary me-1" (click)="editDevice(d)">Editar</button>
                <button class="btn btn-sm btn-outline-danger" (click)="deleteDevice(d.id!)">Eliminar</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="card mt-3" *ngIf="showDeviceForm">
        <div class="card-body">
          <h6>{{ editingDeviceId ? 'Editar' : 'Nuevo' }} dispositivo</h6>
          <form [formGroup]="deviceFormGroup" (ngSubmit)="saveDevice()">
            <div class="row g-2 mb-2">
              <div class="col-12">
                <label class="form-label">Nombre</label>
                <input class="form-control" formControlName="name" placeholder="Nombre">
              </div>
              <div class="col-12">
                <label class="form-label">Descripción</label>
                <textarea class="form-control" formControlName="description" placeholder="Descripción" rows="2"></textarea>
              </div>
              <div class="col-6">
                <label class="form-label">Marca</label>
                <select class="form-select" formControlName="brandId">
                  <option *ngFor="let b of brands" [value]="b.id">{{ b.name }}</option>
                </select>
              </div>
              <div class="col-6">
                <label class="form-label">Tipo</label>
                <select class="form-select" formControlName="deviceTypeId">
                  <option *ngFor="let t of deviceTypes" [value]="t.id">{{ t.name }}</option>
                </select>
              </div>
              <div class="col-6">
                <label class="form-label">Fecha de lanzamiento</label>
                <input type="date" class="form-control" formControlName="releaseDate">
              </div>
            </div>
            <div class="mb-3">
              <label class="form-label d-block">Imágenes (URLs)</label>
              <div formArrayName="images" class="d-flex flex-column gap-2">
                <div *ngFor="let ctrl of imagesArray.controls; let i = index" class="input-group align-items-center">
                  <input class="form-control" [formControlName]="i" placeholder="https://ejemplo.com/imagen.jpg" type="url">
                  <button type="button" class="btn btn-outline-danger" (click)="removeImage(i)" [attr.aria-label]="'Eliminar imagen ' + (i+1)"
                    *ngIf="imagesArray.length > 1">Eliminar</button>
                </div>
              </div>
              <button type="button" class="btn btn-outline-primary btn-sm mt-2" (click)="addImage()">Agregar imagen</button>
            </div>
            <button type="submit" class="btn btn-primary mt-2" [disabled]="deviceFormGroup.invalid">Guardar</button>
            <button type="button" class="btn btn-secondary mt-2 ms-2" (click)="cancelDeviceForm()">Cancelar</button>
          </form>
        </div>
      </div>
    </div>

    <!-- Marcas -->
    <div *ngIf="tab === 'brands'">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h5>CRUD Marcas</h5>
        <button class="btn btn-primary" (click)="openBrandForm()">Nueva marca</button>
      </div>
      <table class="table table-hover">
        <thead><tr><th>Id</th><th>Nombre</th><th>Descripción</th><th></th></tr></thead>
        <tbody>
          <tr *ngFor="let b of brands">
            <td>{{ b.id }}</td>
            <td>{{ b.name }}</td>
            <td>{{ b.description }}</td>
            <td>
              <button class="btn btn-sm btn-outline-primary me-1" (click)="editBrand(b)">Editar</button>
              <button class="btn btn-sm btn-outline-danger" (click)="deleteBrand(b.id!)">Eliminar</button>
            </td>
          </tr>
        </tbody>
      </table>
      <div class="card mt-3" *ngIf="showBrandForm">
        <div class="card-body">
          <form (ngSubmit)="saveBrand()" #bf="ngForm">
            <input class="form-control mb-2" placeholder="Nombre" [(ngModel)]="brandForm.name" name="brandName" required>
            <input class="form-control mb-2" placeholder="Descripción" [(ngModel)]="brandForm.description" name="brandDesc">
            <button type="submit" class="btn btn-primary" [disabled]="bf.invalid">Guardar</button>
            <button type="button" class="btn btn-secondary ms-2" (click)="cancelBrandForm()">Cancelar</button>
          </form>
        </div>
      </div>
    </div>

    <!-- Tipos -->
    <div *ngIf="tab === 'types'">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h5>CRUD Tipos de dispositivo</h5>
        <button class="btn btn-primary" (click)="openTypeForm()">Nuevo tipo</button>
      </div>
      <table class="table table-hover">
        <thead><tr><th>Id</th><th>Nombre</th><th>Descripción</th><th></th></tr></thead>
        <tbody>
          <tr *ngFor="let t of deviceTypes">
            <td>{{ t.id }}</td>
            <td>{{ t.name }}</td>
            <td>{{ t.description }}</td>
            <td>
              <button class="btn btn-sm btn-outline-primary me-1" (click)="editType(t)">Editar</button>
              <button class="btn btn-sm btn-outline-danger" (click)="deleteType(t.id!)">Eliminar</button>
            </td>
          </tr>
        </tbody>
      </table>
      <div class="card mt-3" *ngIf="showTypeForm">
        <div class="card-body">
          <form (ngSubmit)="saveType()" #tf="ngForm">
            <input class="form-control mb-2" placeholder="Nombre" [(ngModel)]="typeForm.name" name="typeName" required>
            <input class="form-control mb-2" placeholder="Descripción" [(ngModel)]="typeForm.description" name="typeDesc">
            <button type="submit" class="btn btn-primary" [disabled]="tf.invalid">Guardar</button>
            <button type="button" class="btn btn-secondary ms-2" (click)="cancelTypeForm()">Cancelar</button>
          </form>
        </div>
      </div>
    </div>
  `,
})
export class AdminComponent implements OnInit {
  tab: 'devices' | 'brands' | 'types' = 'devices';
  devices: Device[] = [];
  brands: Brand[] = [];
  deviceTypes: DeviceType[] = [];

  showDeviceForm = false;
  editingDeviceId: number | null = null;
  deviceFormGroup!: FormGroup;

  showBrandForm = false;
  editingBrandId: number | null = null;
  brandForm: Brand = { name: '', description: '' };

  showTypeForm = false;
  editingTypeId: number | null = null;
  typeForm: DeviceType = { name: '', description: '' };

  constructor(
    private deviceService: DeviceService,
    private catalogService: CatalogService,
    private fb: FormBuilder,
  ) {}

  ngOnInit(): void {
    this.buildDeviceForm();
    this.loadAll();
  }

  private buildDeviceForm(initialImages: string[] = ['']): void {
    this.deviceFormGroup = this.fb.group({
      name: ['', Validators.required],
      description: [''],
      brandId: [null as number | null, Validators.required],
      deviceTypeId: [null as number | null, Validators.required],
      releaseDate: [''],
      images: this.fb.array(initialImages.map(url => this.fb.control(url, []))),
    });
  }

  get imagesArray(): FormArray {
    return this.deviceFormGroup.get('images') as FormArray;
  }

  addImage(): void {
    this.imagesArray.push(this.fb.control('', []));
  }

  removeImage(index: number): void {
    if (this.imagesArray.length > 1) {
      this.imagesArray.removeAt(index);
    }
  }

  loadAll(): void {
    this.deviceService.getList({}).subscribe((d) => (this.devices = d));
    this.catalogService.getBrands().subscribe((b) => (this.brands = b));
    this.catalogService.getDeviceTypes().subscribe((t) => (this.deviceTypes = t));
  }

  getBrandName(id: number): string {
    return this.brands.find((b) => b.id === id)?.name ?? '-';
  }
  getTypeName(id: number): string {
    return this.deviceTypes.find((t) => t.id === id)?.name ?? '-';
  }

  openDeviceForm(): void {
    this.editingDeviceId = null;
    this.buildDeviceForm(['']);
    this.deviceFormGroup.patchValue({
      brandId: this.brands[0]?.id ?? null,
      deviceTypeId: this.deviceTypes[0]?.id ?? null,
    });
    this.showDeviceForm = true;
  }

  editDevice(d: Device): void {
    this.editingDeviceId = d.id!;
    const urls = (d.imageUrls ?? d.images ?? []).length ? (d.imageUrls ?? d.images ?? []) : [''];
    this.buildDeviceForm(urls);
    this.deviceFormGroup.patchValue({
      name: d.name,
      description: d.description ?? '',
      brandId: d.brandId,
      deviceTypeId: d.deviceTypeId,
      releaseDate: d.releaseDate ?? '',
    });
    this.showDeviceForm = true;
  }

  cancelDeviceForm(): void {
    this.showDeviceForm = false;
    this.editingDeviceId = null;
  }

  saveDevice(): void {
    const raw = this.deviceFormGroup.getRawValue();
    const images = (raw.images as string[]).filter((url: string) => url != null && String(url).trim() !== '');
    const payload: Device = {
      name: raw.name,
      description: raw.description || undefined,
      brandId: raw.brandId,
      deviceTypeId: raw.deviceTypeId,
      releaseDate: raw.releaseDate || undefined,
      imageUrl: images[0] || undefined,
      imageUrls: images,
      images,
    };
    if (this.editingDeviceId) {
      this.deviceService.update(this.editingDeviceId, payload).subscribe(() => {
        this.loadAll();
        this.cancelDeviceForm();
      });
    } else {
      this.deviceService.create(payload).subscribe(() => {
        this.loadAll();
        this.cancelDeviceForm();
      });
    }
  }
  deleteDevice(id: number): void {
    if (confirm('¿Eliminar este dispositivo?')) {
      this.deviceService.delete(id).subscribe(() => this.loadAll());
    }
  }

  openBrandForm(): void {
    this.editingBrandId = null;
    this.brandForm = { name: '', description: '' };
    this.showBrandForm = true;
  }
  editBrand(b: Brand): void {
    this.editingBrandId = b.id!;
    this.brandForm = { name: b.name, description: b.description || '' };
    this.showBrandForm = true;
  }
  cancelBrandForm(): void {
    this.showBrandForm = false;
    this.editingBrandId = null;
  }
  saveBrand(): void {
    if (this.editingBrandId) {
      this.catalogService.updateBrand(this.editingBrandId, this.brandForm).subscribe(() => {
        this.loadAll();
        this.cancelBrandForm();
      });
    } else {
      this.catalogService.createBrand(this.brandForm).subscribe(() => {
        this.loadAll();
        this.cancelBrandForm();
      });
    }
  }
  deleteBrand(id: number): void {
    if (confirm('¿Eliminar esta marca?')) {
      this.catalogService.deleteBrand(id).subscribe(() => this.loadAll());
    }
  }

  openTypeForm(): void {
    this.editingTypeId = null;
    this.typeForm = { name: '', description: '' };
    this.showTypeForm = true;
  }
  editType(t: DeviceType): void {
    this.editingTypeId = t.id!;
    this.typeForm = { name: t.name, description: t.description || '' };
    this.showTypeForm = true;
  }
  cancelTypeForm(): void {
    this.showTypeForm = false;
    this.editingTypeId = null;
  }
  saveType(): void {
    if (this.editingTypeId) {
      this.catalogService.updateDeviceType(this.editingTypeId, this.typeForm).subscribe(() => {
        this.loadAll();
        this.cancelTypeForm();
      });
    } else {
      this.catalogService.createDeviceType(this.typeForm).subscribe(() => {
        this.loadAll();
        this.cancelTypeForm();
      });
    }
  }
  deleteType(id: number): void {
    if (confirm('¿Eliminar este tipo?')) {
      this.catalogService.deleteDeviceType(id).subscribe(() => this.loadAll());
    }
  }
}
