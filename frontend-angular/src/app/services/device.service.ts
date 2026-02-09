import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Device } from '../models/device.model';

const DEVICE_API = 'http://localhost:8080/api/devices';

@Injectable({ providedIn: 'root' })
export class DeviceService {

  constructor(private http: HttpClient) {}

  getList(params?: { name?: string; brandId?: number; deviceTypeId?: number; sortByReleaseDate?: boolean }): Observable<Device[]> {
    let httpParams = new HttpParams();
    if (params?.name) httpParams = httpParams.set('name', params.name);
    if (params?.brandId != null) httpParams = httpParams.set('brandId', params.brandId);
    if (params?.deviceTypeId != null) httpParams = httpParams.set('deviceTypeId', params.deviceTypeId);
    if (params?.sortByReleaseDate != null) httpParams = httpParams.set('sortByReleaseDate', params.sortByReleaseDate);
    return this.http.get<Device[]>(DEVICE_API, { params: httpParams });
  }

  getById(id: number): Observable<Device> {
    return this.http.get<Device>(`${DEVICE_API}/${id}`);
  }

  create(device: Device): Observable<Device> {
    return this.http.post<Device>(DEVICE_API, device);
  }

  update(id: number, device: Device): Observable<Device> {
    return this.http.put<Device>(`${DEVICE_API}/${id}`, device);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${DEVICE_API}/${id}`);
  }
}
