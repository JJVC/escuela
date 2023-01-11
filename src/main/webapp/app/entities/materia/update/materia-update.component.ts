import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { MateriaFormService, MateriaFormGroup } from './materia-form.service';
import { IMateria } from '../materia.model';
import { MateriaService } from '../service/materia.service';

@Component({
  selector: 'jhi-materia-update',
  templateUrl: './materia-update.component.html',
})
export class MateriaUpdateComponent implements OnInit {
  isSaving = false;
  materia: IMateria | null = null;

  editForm: MateriaFormGroup = this.materiaFormService.createMateriaFormGroup();

  constructor(
    protected materiaService: MateriaService,
    protected materiaFormService: MateriaFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ materia }) => {
      this.materia = materia;
      if (materia) {
        this.updateForm(materia);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const materia = this.materiaFormService.getMateria(this.editForm);
    if (materia.id !== null) {
      this.subscribeToSaveResponse(this.materiaService.update(materia));
    } else {
      this.subscribeToSaveResponse(this.materiaService.create(materia));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMateria>>): void {
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

  protected updateForm(materia: IMateria): void {
    this.materia = materia;
    this.materiaFormService.resetForm(this.editForm, materia);
  }
}
