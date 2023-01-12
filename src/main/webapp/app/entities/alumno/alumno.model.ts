import { ICalificacion } from '../calificacion/calificacion.model';

export interface IAlumno {
  id: number;
  nombre?: string | null;
  apPaterno?: string | null;
  apMaterno?: string | null;
  display?: string | null;
  activo?: boolean | null;
  calificacions?: ICalificacion[];
  promedio?: number;
}

export type NewAlumno = Omit<IAlumno, 'id'> & { id: null };
