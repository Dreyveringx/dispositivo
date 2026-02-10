import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comment } from '../models/comment.model';

import { ApiRoutes } from '../core/api-routes';

@Injectable({ providedIn: 'root' })
export class CommentService {

  constructor(private http: HttpClient) {}

  getByDeviceId(deviceId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(ApiRoutes.comments, { params: new HttpParams().set('deviceId', deviceId) });
  }

  create(comment: Comment): Observable<Comment> {
    return this.http.post<Comment>(ApiRoutes.comments, comment);
  }
}
