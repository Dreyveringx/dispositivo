import { ProblemDetail } from '../models/problem-detail.model';
import { getDisplayMessage } from './error-messages';

export class AppHttpError extends Error {
  constructor(
    message: string,
    public readonly status: number,
    public readonly problemDetail?: ProblemDetail | null
  ) {
    super(message);
    this.name = 'AppHttpError';
    Object.setPrototypeOf(this, AppHttpError.prototype);
  }

  get displayMessage(): string {
    return getDisplayMessage(this.problemDetail ?? null, this.status) || this.message || 'Ha ocurrido un error';
  }
}
