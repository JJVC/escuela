import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'alumno',
        data: { pageTitle: 'escuelaApp.alumno.home.title' },
        loadChildren: () => import('./alumno/alumno.module').then(m => m.AlumnoModule),
      },
      {
        path: 'materia',
        data: { pageTitle: 'escuelaApp.materia.home.title' },
        loadChildren: () => import('./materia/materia.module').then(m => m.MateriaModule),
      },
      {
        path: 'calificacion',
        data: { pageTitle: 'escuelaApp.calificacion.home.title' },
        loadChildren: () => import('./calificacion/calificacion.module').then(m => m.CalificacionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
