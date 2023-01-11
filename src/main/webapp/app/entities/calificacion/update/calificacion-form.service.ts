import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICalificacion, NewCalificacion } from '../calificacion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICalificacion for edit and NewCalificacionFormGroupInput for create.
 */
type CalificacionFormGroupInput = ICalificacion | PartialWithRequiredKeyOf<NewCalificacion>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICalificacion | NewCalificacion> = Omit<T, 'fechaRegistro'> & {
  fechaRegistro?: string | null;
};

type CalificacionFormRawValue = FormValueOf<ICalificacion>;

type NewCalificacionFormRawValue = FormValueOf<NewCalificacion>;

type CalificacionFormDefaults = Pick<NewCalificacion, 'id' | 'fechaRegistro'>;

type CalificacionFormGroupContent = {
  id: FormControl<CalificacionFormRawValue['id'] | NewCalificacion['id']>;
  calificacion: FormControl<CalificacionFormRawValue['calificacion']>;
  fechaRegistro: FormControl<CalificacionFormRawValue['fechaRegistro']>;
  materia: FormControl<CalificacionFormRawValue['materia']>;
  alumno: FormControl<CalificacionFormRawValue['alumno']>;
};

export type CalificacionFormGroup = FormGroup<CalificacionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CalificacionFormService {
  createCalificacionFormGroup(calificacion: CalificacionFormGroupInput = { id: null }): CalificacionFormGroup {
    const calificacionRawValue = this.convertCalificacionToCalificacionRawValue({
      ...this.getFormDefaults(),
      ...calificacion,
    });
    return new FormGroup<CalificacionFormGroupContent>({
      id: new FormControl(
        { value: calificacionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      calificacion: new FormControl(calificacionRawValue.calificacion, {
        validators: [Validators.required],
      }),
      fechaRegistro: new FormControl(calificacionRawValue.fechaRegistro),
      materia: new FormControl(calificacionRawValue.materia),
      alumno: new FormControl(calificacionRawValue.alumno),
    });
  }

  getCalificacion(form: CalificacionFormGroup): ICalificacion | NewCalificacion {
    return this.convertCalificacionRawValueToCalificacion(form.getRawValue() as CalificacionFormRawValue | NewCalificacionFormRawValue);
  }

  resetForm(form: CalificacionFormGroup, calificacion: CalificacionFormGroupInput): void {
    const calificacionRawValue = this.convertCalificacionToCalificacionRawValue({ ...this.getFormDefaults(), ...calificacion });
    form.reset(
      {
        ...calificacionRawValue,
        id: { value: calificacionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CalificacionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaRegistro: currentTime,
    };
  }

  private convertCalificacionRawValueToCalificacion(
    rawCalificacion: CalificacionFormRawValue | NewCalificacionFormRawValue
  ): ICalificacion | NewCalificacion {
    return {
      ...rawCalificacion,
      fechaRegistro: dayjs(rawCalificacion.fechaRegistro, DATE_TIME_FORMAT),
    };
  }

  private convertCalificacionToCalificacionRawValue(
    calificacion: ICalificacion | (Partial<NewCalificacion> & CalificacionFormDefaults)
  ): CalificacionFormRawValue | PartialWithRequiredKeyOf<NewCalificacionFormRawValue> {
    return {
      ...calificacion,
      fechaRegistro: calificacion.fechaRegistro ? calificacion.fechaRegistro.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
