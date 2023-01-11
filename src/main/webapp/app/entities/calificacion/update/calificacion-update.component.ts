import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CalificacionFormService, CalificacionFormGroup } from './calificacion-form.service';
import { ICalificacion } from '../calificacion.model';
import { CalificacionService } from '../service/calificacion.service';
import { IMateria } from 'app/entities/materia/materia.model';
import { MateriaService } from 'app/entities/materia/service/materia.service';
import { IAlumno } from 'app/entities/alumno/alumno.model';
import { AlumnoService } from 'app/entities/alumno/service/alumno.service';

@Component({
  selector: 'jhi-calificacion-update',
  templateUrl: './calificacion-update.component.html',
})
export class CalificacionUpdateComponent implements OnInit {
  isSaving = false;
  calificacion: ICalificacion | null = null;

  materiasSharedCollection: IMateria[] = [];
  alumnosSharedCollection: IAlumno[] = [];

  editForm: CalificacionFormGroup = this.calificacionFormService.createCalificacionFormGroup();

  constructor(
    protected calificacionService: CalificacionService,
    protected calificacionFormService: CalificacionFormService,
    protected materiaService: MateriaService,
    protected alumnoService: AlumnoService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareMateria = (o1: IMateria | null, o2: IMateria | null): boolean => this.materiaService.compareMateria(o1, o2);

  compareAlumno = (o1: IAlumno | null, o2: IAlumno | null): boolean => this.alumnoService.compareAlumno(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ calificacion }) => {
      this.calificacion = calificacion;
      if (calificacion) {
        this.updateForm(calificacion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const calificacion = this.calificacionFormService.getCalificacion(this.editForm);
    if (calificacion.id !== null) {
      this.subscribeToSaveResponse(this.calificacionService.update(calificacion));
    } else {
      this.subscribeToSaveResponse(this.calificacionService.create(calificacion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICalificacion>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(calificacion: ICalificacion): void {
    this.calificacion = calificacion;
    this.calificacionFormService.resetForm(this.editForm, calificacion);

    this.materiasSharedCollection = this.materiaService.addMateriaToCollectionIfMissing<IMateria>(
      this.materiasSharedCollection,
      calificacion.materia
    );
    this.alumnosSharedCollection = this.alumnoService.addAlumnoToCollectionIfMissing<IAlumno>(
      this.alumnosSharedCollection,
      calificacion.alumno
    );
  }

  protected loadRelationshipsOptions(): void {
    this.materiaService
      .query()
      .pipe(map((res: HttpResponse<IMateria[]>) => res.body ?? []))
      .pipe(
        map((materias: IMateria[]) => this.materiaService.addMateriaToCollectionIfMissing<IMateria>(materias, this.calificacion?.materia))
      )
      .subscribe((materias: IMateria[]) => (this.materiasSharedCollection = materias));

    this.alumnoService
      .query()
      .pipe(map((res: HttpResponse<IAlumno[]>) => res.body ?? []))
      .pipe(map((alumnos: IAlumno[]) => this.alumnoService.addAlumnoToCollectionIfMissing<IAlumno>(alumnos, this.calificacion?.alumno)))
      .subscribe((alumnos: IAlumno[]) => (this.alumnosSharedCollection = alumnos));
  }
}
