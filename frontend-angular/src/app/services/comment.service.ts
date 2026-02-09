import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comment } from '../models/comment.model';

const COMMENT_API = 'http://localhost:8082/api/comments';

@Injectable({ providedIn: 'root' })
export class CommentService {

  constructor(private http: HttpClient) {}

  getByDeviceId(deviceId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(COMMENT_API, { params: new HttpParams().set('deviceId', deviceId) });
  }

  create(comment: Comment): Observable<Comment> {
    return this.http.post<Comment>(COMMENT_API, comment);
  }
}
