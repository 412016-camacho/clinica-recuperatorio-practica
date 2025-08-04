import {Component, EventEmitter, inject, Input, OnChanges, OnInit, Output} from '@angular/core';
import {FormsModule, NgForm} from "@angular/forms";
import {Turno, TurnoInput} from "../../models/turno.model";
import {Estudio} from "../../models/estudio.model";
import {Paciente} from "../../models/paciente.model";
import {Extractor} from "../../models/extractor.model";
import {TurnoService} from "../../services/turno.service";
import {PacienteService} from "../../services/paciente.service";
import {EstudioService} from "../../services/estudio.service";
import {ExtractorService} from "../../services/extractor.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-alta-turno',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './alta-turno.component.html'
})
export class AltaTurnoComponent implements OnInit, OnChanges {
  private turnoService = inject(TurnoService);
  private pacienteService = inject(PacienteService);
  private estudioService = inject(EstudioService);
  private extractorService = inject(ExtractorService);
  private router = inject(Router);

  @Output() turnoGuardado = new EventEmitter<void>();
  @Input() turno: Turno = {
    id: 0,
    paciente: {
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
  };
  pacientes: Paciente[] = [];
  extractores: Extractor[] = [];
  estudios: Estudio[] = [];
  idExtractor: number = 0;
  idPaciente: number = 0;
  idEstudio: number = 0;

  ngOnInit(): void {
    this.cargarPacientes();
    this.cargarEstudios();
    this.cargarExtractores();
  }

  guardarTurno(myForm: NgForm) {
    if(myForm.invalid){
      alert("Faltan algunos campos");
      return;
    }

    // Normalizar fechaHora a yyyy-MM-dd'T'HH:mm:ss
    let fechaHoraFormateada = this.turno.fechaHora;
    if (fechaHoraFormateada.length === 16) {
      fechaHoraFormateada += ':00'; // Agregar los segundos si no están
    }

    const turnoInput: TurnoInput = {
      paciente_id: this.idPaciente,
      estudio_id: this.idEstudio,
      extractor_id: this.idExtractor,
      fechaHora: fechaHoraFormateada,
      observaciones: this.turno.observaciones
    }
    this.turnoService.altaTurno(turnoInput).subscribe({
      next: (data)=> {
        console.log("Turno creado: ",data);
        myForm.resetForm();
        this.turnoGuardado.emit();
      }, error: error => {
        console.log(error);
      }
    })
  }


  ngOnChanges(): void {
    if(this.turno.extractor?.id){
      this.idExtractor = this.turno.extractor.id;
    }
    if(this.turno.estudio?.id){
      this.idEstudio = this.turno.estudio.id;
    }
    if(this.turno.paciente?.id){
      this.idPaciente = this.turno.paciente.id;
    }
  }


  private cargarPacientes() {
    this.pacienteService.getPacientes().subscribe({
      next: data => {
        this.pacientes = data;
      },
      error: err => {
        console.log(err);
      }
    })
  }

  private cargarEstudios() {
    this.estudioService.getEstudios().subscribe({
      next: data => {
        this.estudios = data;
      },
      error: err => {
        console.log(err);
      }
    })
  }

  private cargarExtractores() {
    this.extractorService.getExtractores().subscribe({
      next: data => {
        this.extractores = data;
      },
      error: err => {
        console.log(err);
      }
    })
  }

  deshabilitarBoton() {
    if (this.idPaciente === 0 || this.idEstudio === 0 || this.idExtractor === 0) {
      return true;
    }
    if (!this.turno.fechaHora || this.turno.fechaHora.trim() === '') {
      return true;
    }
    return false;
  }
}
