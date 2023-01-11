import dayjs from 'dayjs/esm';
import { IMateria } from 'app/entities/materia/materia.model';
import { IAlumno } from 'app/entities/alumno/alumno.model';

export interface ICalificacion {
  id: number;
  calificacion?: number | null;
  fechaRegistro?: dayjs.Dayjs | null;
  materia?: Pick<IMateria, 'id' | 'nombre'> | null;
  alumno?: Pick<IAlumno, 'id' | 'display'> | null;
}

export type NewCalificacion = Omit<ICalificacion, 'id'> & { id: null };
