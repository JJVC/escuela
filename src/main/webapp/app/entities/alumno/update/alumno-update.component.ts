import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { AlumnoFormService, AlumnoFormGroup } from './alumno-form.service';
import { IAlumno } from '../alumno.model';
import { AlumnoService } from '../service/alumno.service';

@Component({
  selector: 'jhi-alumno-update',
  templateUrl: './alumno-update.component.html',
})
export class AlumnoUpdateComponent implements OnInit {
  isSaving = false;
  alumno: IAlumno | null = null;

  editForm: AlumnoFormGroup = this.alumnoFormService.createAlumnoFormGroup();

  constructor(
    protected alumnoService: AlumnoService,
    protected alumnoFormService: AlumnoFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ alumno }) => {
      this.alumno = alumno;
      if (alumno) {
        this.updateForm(alumno);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const alumno = this.alumnoFormService.getAlumno(this.editForm);
    if (alumno.id !== null) {
      this.subscribeToSaveResponse(this.alumnoService.update(alumno));
    } else {
      this.subscribeToSaveResponse(this.alumnoService.create(alumno));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlumno>>): void {
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

  protected updateForm(alumno: IAlumno): void {
    this.alumno = alumno;
    this.alumnoFormService.resetForm(this.editForm, alumno);
  }
}
