import { Component } from '@angular/core';
import { TurnoService } from '../../services/turno.service';
import { FormsModule } from '@angular/forms';
import { CommonModule, NgIf } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-programar-turnos',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './programar-turnos.component.html'})

export class ProgramarTurnosComponent {
  fecha: string = '';
  mensaje: string = '';
  error: string = '';
  cargando = false;

  constructor(private tSrv: TurnoService, private router: Router) { }

  programar() {
    this.mensaje = this.error = '';
    if (!this.fecha) { this.error = "Debe ingresar una fecha"; return; }
    this.cargando = true;
    this.tSrv.programarTurnos(this.fecha).subscribe({
      next: _ => {
        this.mensaje = "Turnos programados correctamente!";
        this.cargando = false;
        this.router.navigate(['/turnos-disponibles']);
      },
      error: err => { this.error = err.error?.message || 'Error al programar'; this.cargando = false; }
    });
  }
}
