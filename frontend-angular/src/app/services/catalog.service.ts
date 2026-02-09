import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Brand } from '../models/brand.model';
import { DeviceType } from '../models/device-type.model';

const CATALOG_API = 'http://localhost:8081/api';

@Injectable({ providedIn: 'root' })
export class CatalogService {

  constructor(private http: HttpClient) {}

  getBrands(): Observable<Brand[]> {
    return this.http.get<Brand[]>(`${CATALOG_API}/brands`);
  }

  getBrandById(id: number): Observable<Brand> {
    return this.http.get<Brand>(`${CATALOG_API}/brands/${id}`);
  }

  createBrand(brand: Brand): Observable<Brand> {
    return this.http.post<Brand>(`${CATALOG_API}/brands`, brand);
  }

  updateBrand(id: number, brand: Brand): Observable<Brand> {
    return this.http.put<Brand>(`${CATALOG_API}/brands/${id}`, brand);
  }

  deleteBrand(id: number): Observable<void> {
    return this.http.delete<void>(`${CATALOG_API}/brands/${id}`);
  }

  getDeviceTypes(): Observable<DeviceType[]> {
    return this.http.get<DeviceType[]>(`${CATALOG_API}/device-types`);
  }

  getDeviceTypeById(id: number): Observable<DeviceType> {
    return this.http.get<DeviceType>(`${CATALOG_API}/device-types/${id}`);
  }

  createDeviceType(deviceType: DeviceType): Observable<DeviceType> {
    return this.http.post<DeviceType>(`${CATALOG_API}/device-types`, deviceType);
  }

  updateDeviceType(id: number, deviceType: DeviceType): Observable<DeviceType> {
    return this.http.put<DeviceType>(`${CATALOG_API}/device-types/${id}`, deviceType);
  }

  deleteDeviceType(id: number): Observable<void> {
    return this.http.delete<void>(`${CATALOG_API}/device-types/${id}`);
  }
}
