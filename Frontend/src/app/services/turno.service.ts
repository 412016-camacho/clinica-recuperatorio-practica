import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FiltroTurnos, Turno, TurnoInput } from '../models/turno.model';
import {ApiConfig} from "./config-api";

@Injectable({ providedIn: 'root' })
export class TurnoService {
  constructor(private http: HttpClient) { }

  getTurnos(filtro: FiltroTurnos): Observable<Turno[]> {
    let params = new HttpParams();
    if (filtro) {
      if (filtro.paciente_id) params = params.set('paciente_id', filtro.paciente_id);
      if (filtro.estudio_id) params = params.set('estudio_id', filtro.estudio_id);
      if (filtro.extractor_id) params = params.set('extractor_id', filtro.extractor_id);
      if (filtro.fecha) params = params.set('fecha', filtro.fecha);
    }
    return this.http.get<Turno[]>(ApiConfig.TURNOS, { params });
  }

  getAllTurnos(): Observable<Turno[]> {
    return this.http.get<Turno[]>(ApiConfig.TURNOS);
  }

  altaTurno(datos: TurnoInput): Observable<Turno> {
    return this.http.put<Turno>(ApiConfig.TURNOS, datos);
  }

  programarTurnos(fecha: string): Observable<any> {
    return this.http.post(ApiConfig.programarTurnos(fecha), {});
  }
}
