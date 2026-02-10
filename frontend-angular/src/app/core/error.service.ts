import { Injectable, signal, computed } from '@angular/core';
import { ProblemDetail } from '../models/problem-detail.model';
import { getDisplayMessage } from './error-messages';

/**
 * Centralized error state for HTTP errors (RFC 7807).
 * The interceptor sets the error; components or a global banner can read it.
 */
@Injectable({ providedIn: 'root' })
export class ErrorService {
  private readonly lastError = signal<ProblemDetail | null>(null);

  readonly currentError = computed(() => this.lastError());
  readonly hasError = computed(() => this.lastError() != null);

  setError(problem: ProblemDetail | null): void {
    this.lastError.set(problem);
  }

  clear(): void {
    this.lastError.set(null);
  }

  getDisplayMessage(problem: ProblemDetail | null): string {
    return getDisplayMessage(problem);
  }
}
