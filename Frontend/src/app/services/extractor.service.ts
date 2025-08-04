import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Extractor } from '../models/extractor.model';
import {ApiConfig} from "./config-api";
import {Turno} from "../models/turno.model";

@Injectable({ providedIn: 'root' })
export class ExtractorService {
  constructor(private http: HttpClient) { }

  getExtractores(): Observable<Extractor[]> {
    return this.http.get<Extractor[]>(ApiConfig.EXTRACTORES);
  }
  getTurnosExtractor(extractorId: number): Observable<Turno[]> {
    return this.http.get<Turno[]>(`${ApiConfig.EXTRACTORES}/${extractorId}/turnos`);
  }
}
