import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Estudio } from '../models/estudio.model';
import {ApiConfig} from "./config-api";

@Injectable({ providedIn: 'root' })
export class EstudioService {
  constructor(private http: HttpClient) { }

  getEstudios(): Observable<Estudio[]> {
    return this.http.get<Estudio[]>(ApiConfig.ESTUDIOS);
  }
}
