import { Paciente } from './paciente.model';
import { Estudio } from './estudio.model';
import { Extractor } from './extractor.model';

export interface Turno {
  id: number;
  paciente?: Paciente;
  estudio?: Estudio;
  extractor?: Extractor;
  fechaHora: string; // yyyy-MM-dd'T'HH:mm:ss
  observaciones?: string;
  status?: string;
}

export interface TurnoInput {
  paciente_id: number;
  estudio_id: number;
  extractor_id: number;
  fechaHora: string;       // yyyy-MM-dd'T'HH:mm:ss
  observaciones?: string;
}

export interface FiltroTurnos {
  paciente_id?: number;
  estudio_id?: number;
  extractor_id?: number;
  fecha?: string; // yyyy-MM-dd'T'HH:mm:ss
}
