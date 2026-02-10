import { ProblemDetail } from '../models/problem-detail.model';

/**
 * Punto único para obtener mensaje de error mostrable al usuario (RFC 7807).
 * Usado por ErrorService y AppHttpError para evitar duplicar lógica.
 */
export function getDisplayMessage(problem: ProblemDetail | null, fallbackStatus?: number): string {
  if (problem?.detail) return problem.detail;
  if (problem?.title) return problem.title;
  const status = problem?.status ?? fallbackStatus;
  if (status === 404) return 'Recurso no encontrado';
  if (status != null && status >= 500) return 'Error del servidor. Intente más tarde.';
  return 'Ha ocurrido un error';
}
