import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { ErrorService } from './error.service';
import { ProblemDetail } from '../models/problem-detail.model';
import { AppHttpError } from './http-error';

export const problemDetailInterceptor: HttpInterceptorFn = (req, next) => {
  const errorService = inject(ErrorService);

  return next(req).pipe(
    catchError((err: unknown) => {
      if (err instanceof HttpErrorResponse) {
        const problem = tryParseProblemDetail(err);
        errorService.setError(problem);
        const message = problem?.detail ?? problem?.title ?? err.message ?? 'Request failed';
        return throwError(() => new AppHttpError(message, err.status, problem ?? undefined));
      }
      errorService.setError(null);
      return throwError(() => err);
    })
  );
};

function tryParseProblemDetail(response: HttpErrorResponse): ProblemDetail | null {
  const body = response.error;
  if (body == null) return null;
  if (typeof body !== 'object') return null;
  return {
    type: body.type,
    title: body.title,
    status: body.status ?? response.status,
    detail: body.detail,
    instance: body.instance,
    errors: body.errors,
  };
}
