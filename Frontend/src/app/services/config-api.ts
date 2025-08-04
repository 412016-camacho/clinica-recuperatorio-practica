// src/app/config-api.ts

export class ApiConfig {
  /** URL base del backend */
  static readonly API_BASE = 'http://localhost:8080';

  // Endpoints recursos
  static readonly TURNOS = `${ApiConfig.API_BASE}/api/v1/turnos`;
  static readonly PACIENTES = `${ApiConfig.API_BASE}/api/v1/pacientes`;
  static readonly ESTUDIOS = `${ApiConfig.API_BASE}/api/v1/estudios`;
  static readonly EXTRACTORES = `${ApiConfig.API_BASE}/api/v1/extractores`;

  /** Endpoint para programar turnos vacíos para todos los extractores en una fecha */
  static programarTurnos(date: string) {
    return `${ApiConfig.TURNOS}/programar?fecha=${date}`;
  }
}
