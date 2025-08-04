import {Component, Input, OnInit} from '@angular/core';
import { TurnoService } from '../../services/turno.service';
import { CommonModule, DatePipe, NgClass } from '@angular/common';
import { TurnosFilterComponent } from '../turnos-filter/turnos-filter.component';
import { FiltroTurnos, Turno } from '../../models/turno.model';
import {ExtractorService} from "../../services/extractor.service";
import {map} from "rxjs";

@Component({
  selector: 'app-turnos-list',
  standalone: true,
  imports: [
    DatePipe,
    TurnosFilterComponent,
    CommonModule
  ],
  templateUrl: './turnos-list.component.html'
})
export class TurnosListComponent implements OnInit {
  @Input() turnos: Turno[] = [];
  filtro: FiltroTurnos = {};
  error: string = '';

  constructor(private tSrv: TurnoService, private eSrv: ExtractorService) { }

  ngOnInit() {
    this.cargarTurnos({});
  }

  onFiltrar(obj: FiltroTurnos) {
    this.filtro = obj;
    this.cargarTurnos(obj);
  }

  cargarTurnos(obj: FiltroTurnos) {
    //TODO: Obtener los turnos que solo tengan el status 'OCUPADO'
    this.tSrv.getTurnos(obj).subscribe({
      next: data => {
        this.turnos = data.filter(t => t.status === 'OCUPADO');
      },
      error: err => {
        alert('Error al cargar' + err);
      }
    })
  }


}
