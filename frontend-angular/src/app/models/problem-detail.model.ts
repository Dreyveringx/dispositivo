/**
 * RFC 7807 Problem Details for HTTP APIs.
 * Aligned with backend GlobalExceptionHandler / ProblemDetail.
 */
export interface ProblemDetail {
  type?: string;
  title?: string;
  status?: number;
  detail?: string;
  instance?: string;
  /** Validation errors: field name -> message (when status 400) */
  errors?: Record<string, string>;
}
