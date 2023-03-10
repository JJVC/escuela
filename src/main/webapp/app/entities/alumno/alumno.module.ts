import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AlumnoComponent } from './list/alumno.component';
import { AlumnoDetailComponent } from './detail/alumno-detail.component';
import { AlumnoUpdateComponent } from './update/alumno-update.component';
import { AlumnoDeleteDialogComponent } from './delete/alumno-delete-dialog.component';
import { AlumnoRoutingModule } from './route/alumno-routing.module';
import { CalificacionesComponent } from './calificaciones/calificaciones.component';

@NgModule({
  imports: [SharedModule, AlumnoRoutingModule],
  declarations: [AlumnoComponent, AlumnoDetailComponent, AlumnoUpdateComponent, AlumnoDeleteDialogComponent, CalificacionesComponent],
})
export class AlumnoModule {}
