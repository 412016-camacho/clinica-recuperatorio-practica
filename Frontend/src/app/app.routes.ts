import {Routes} from '@angular/router';
import {TurnosListComponent} from './components/turnos-list/turnos-list.component';
import {ProgramarTurnosComponent} from './components/programar-turnos/programar-turnos.component';
import {TurnosDisponiblesComponent} from './components/turnos-disponibles/turnos-disponibles.component';

export const routes: Routes = [
  {path: '', component: TurnosDisponiblesComponent},
  {path: 'turnos', component: TurnosListComponent},
  {path: 'turnos-disponibles', component: TurnosDisponiblesComponent},
  {path: 'programar-turno', component: ProgramarTurnosComponent},
  {path: "**", redirectTo: "turnos"},
];
