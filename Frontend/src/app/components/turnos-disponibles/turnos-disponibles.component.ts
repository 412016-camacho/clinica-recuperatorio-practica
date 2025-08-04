import {Component, OnInit} from '@angular/core';
import { AltaTurnoComponent } from '../alta-turno/alta-turno.component';
import { Turno} from "../../models/turno.model";
import {DatePipe} from "@angular/common";
import {TurnosListComponent} from "../turnos-list/turnos-list.component";
import {TurnoService} from "../../services/turno.service";
import {ExtractorService} from "../../services/extractor.service";
import {FormsModule} from "@angular/forms";
import {Extractor} from "../../models/extractor.model";
import {Router} from "@angular/router";

@Component({
    selector: 'app-turnos-disponibles',
    standalone: true,
  imports: [AltaTurnoComponent, DatePipe, FormsModule],
    templateUrl: './turnos-disponibles.component.html',
})
export class TurnosDisponiblesComponent implements OnInit {
  turnos: Turno[] = [];
  extractores: Extractor[] = [];
  selected = false;
  turnoSeleccionado: Turno = {
    id: 0,
    paciente : {
      id: 0,
      nombre_completo: '',
      fecha_nacimiento: ''
    },
    estudio: {
      id: 0,
      nombre_estudio: '',
      descripcion: ''
    },
    extractor: {
      id: 0,
      nombre_completo: '',
      matricula: ''
    },
    fechaHora: '',
    observaciones:'',
    status: ''
  }

  constructor(private tSrv: TurnoService, private eSrv: ExtractorService, private router: Router) { }

  ngOnInit(): void {
    this.cargarTurnosDisponibles();
    this.cargarExtractores();
  }

  cargarExtractores(){
    this.eSrv.getExtractores().subscribe({
      next: data => {
        this.extractores = data;
      }, error: err => {
        alert(err);
      }
    })
  }


  onExtractorSeleccionado(event: EventTarget | null) {
    const seleccionado = event as HTMLSelectElement; //necesario para poder acceder a .value
    const id = +seleccionado.value;   //parsear a numero (+)

    if(id === 0){
      this.cargarTurnosDisponibles();
    }else{
      this.eSrv.getTurnosExtractor(id).subscribe({
        next: data => {
          this.turnos = data;
        }, error: err => {
          alert(err);
        }
      })
    }

  }


  cargarTurnosDisponibles() {
    this.tSrv.getAllTurnos().subscribe({
      next: (data: Turno[]) => {
        this.turnos = data.filter(t => t.status === 'DISPONIBLE');
      },
      error: (err) => {
        this.turnos = [];
        alert("Error en el turno " + err);
      }
    })
  }

  seleccionarExtractor(t:Turno) {
    this.selected = true;
    this.turnoSeleccionado = t;
  }

  cancelar() {
    this.turnoSeleccionado = {
      id: 0,
      paciente : {
        id: -1,
        nombre_completo: '',
        fecha_nacimiento: ''
      },
      estudio: {
        id: -1,
        nombre_estudio: '',
        descripcion: ''
      },
      extractor: {
        id: -1,
        nombre_completo: '',
        matricula: ''
      },
      fechaHora: '',
      observaciones:'',
      status: ''
    }
  }

  onTurnoGuardado() {
    this.router.navigate(['turnos']);
  }
}
