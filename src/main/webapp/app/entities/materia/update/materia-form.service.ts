import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMateria, NewMateria } from '../materia.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMateria for edit and NewMateriaFormGroupInput for create.
 */
type MateriaFormGroupInput = IMateria | PartialWithRequiredKeyOf<NewMateria>;

type MateriaFormDefaults = Pick<NewMateria, 'id' | 'activo'>;

type MateriaFormGroupContent = {
  id: FormControl<IMateria['id'] | NewMateria['id']>;
  nombre: FormControl<IMateria['nombre']>;
  activo: FormControl<IMateria['activo']>;
};

export type MateriaFormGroup = FormGroup<MateriaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MateriaFormService {
  createMateriaFormGroup(materia: MateriaFormGroupInput = { id: null }): MateriaFormGroup {
    const materiaRawValue = {
      ...this.getFormDefaults(),
      ...materia,
    };
    return new FormGroup<MateriaFormGroupContent>({
      id: new FormControl(
        { value: materiaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombre: new FormControl(materiaRawValue.nombre, {
        validators: [Validators.required],
      }),
      activo: new FormControl(materiaRawValue.activo),
    });
  }

  getMateria(form: MateriaFormGroup): IMateria | NewMateria {
    return form.getRawValue() as IMateria | NewMateria;
  }

  resetForm(form: MateriaFormGroup, materia: MateriaFormGroupInput): void {
    const materiaRawValue = { ...this.getFormDefaults(), ...materia };
    form.reset(
      {
        ...materiaRawValue,
        id: { value: materiaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MateriaFormDefaults {
    return {
      id: null,
      activo: false,
    };
  }
}
