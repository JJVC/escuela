import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAlumno, NewAlumno } from '../alumno.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAlumno for edit and NewAlumnoFormGroupInput for create.
 */
type AlumnoFormGroupInput = IAlumno | PartialWithRequiredKeyOf<NewAlumno>;

type AlumnoFormDefaults = Pick<NewAlumno, 'id' | 'activo'>;

type AlumnoFormGroupContent = {
  id: FormControl<IAlumno['id'] | NewAlumno['id']>;
  nombre: FormControl<IAlumno['nombre']>;
  apPaterno: FormControl<IAlumno['apPaterno']>;
  apMaterno: FormControl<IAlumno['apMaterno']>;
  display: FormControl<IAlumno['display']>;
  activo: FormControl<IAlumno['activo']>;
};

export type AlumnoFormGroup = FormGroup<AlumnoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AlumnoFormService {
  createAlumnoFormGroup(alumno: AlumnoFormGroupInput = { id: null }): AlumnoFormGroup {
    const alumnoRawValue = {
      ...this.getFormDefaults(),
      ...alumno,
    };
    return new FormGroup<AlumnoFormGroupContent>({
      id: new FormControl(
        { value: alumnoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombre: new FormControl(alumnoRawValue.nombre, {
        validators: [Validators.required],
      }),
      apPaterno: new FormControl(alumnoRawValue.apPaterno, {
        validators: [Validators.required],
      }),
      apMaterno: new FormControl(alumnoRawValue.apMaterno),
      display: new FormControl(alumnoRawValue.display),
      activo: new FormControl(alumnoRawValue.activo),
    });
  }

  getAlumno(form: AlumnoFormGroup): IAlumno | NewAlumno {
    return form.getRawValue() as IAlumno | NewAlumno;
  }

  resetForm(form: AlumnoFormGroup, alumno: AlumnoFormGroupInput): void {
    const alumnoRawValue = { ...this.getFormDefaults(), ...alumno };
    form.reset(
      {
        ...alumnoRawValue,
        id: { value: alumnoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AlumnoFormDefaults {
    return {
      id: null,
      activo: false,
    };
  }
}
