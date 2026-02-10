import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Brand } from '../models/brand.model';
import { DeviceType } from '../models/device-type.model';

import { ApiRoutes } from '../core/api-routes';

@Injectable({ providedIn: 'root' })
export class CatalogService {

  constructor(private http: HttpClient) {}

  getBrands(): Observable<Brand[]> {
    return this.http.get<Brand[]>(ApiRoutes.brands);
  }

  getBrandById(id: number): Observable<Brand> {
    return this.http.get<Brand>(`${ApiRoutes.brands}/${id}`);
  }

  createBrand(brand: Brand): Observable<Brand> {
    return this.http.post<Brand>(ApiRoutes.brands, brand);
  }

  updateBrand(id: number, brand: Brand): Observable<Brand> {
    return this.http.put<Brand>(`${ApiRoutes.brands}/${id}`, brand);
  }

  deleteBrand(id: number): Observable<void> {
    return this.http.delete<void>(`${ApiRoutes.brands}/${id}`);
  }

  getDeviceTypes(): Observable<DeviceType[]> {
    return this.http.get<DeviceType[]>(ApiRoutes.deviceTypes);
  }

  getDeviceTypeById(id: number): Observable<DeviceType> {
    return this.http.get<DeviceType>(`${ApiRoutes.deviceTypes}/${id}`);
  }

  createDeviceType(deviceType: DeviceType): Observable<DeviceType> {
    return this.http.post<DeviceType>(ApiRoutes.deviceTypes, deviceType);
  }

  updateDeviceType(id: number, deviceType: DeviceType): Observable<DeviceType> {
    return this.http.put<DeviceType>(`${ApiRoutes.deviceTypes}/${id}`, deviceType);
  }

  deleteDeviceType(id: number): Observable<void> {
    return this.http.delete<void>(`${ApiRoutes.deviceTypes}/${id}`);
  }
}
