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
  templateUrl: './admin.component.html',
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
