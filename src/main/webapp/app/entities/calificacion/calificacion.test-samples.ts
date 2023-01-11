import dayjs from 'dayjs/esm';

import { ICalificacion, NewCalificacion } from './calificacion.model';

export const sampleWithRequiredData: ICalificacion = {
  id: 37751,
  calificacion: 44374,
};

export const sampleWithPartialData: ICalificacion = {
  id: 80668,
  calificacion: 88411,
  fechaRegistro: dayjs('2023-01-10T12:38'),
};

export const sampleWithFullData: ICalificacion = {
  id: 81293,
  calificacion: 36293,
  fechaRegistro: dayjs('2023-01-11T03:53'),
};

export const sampleWithNewData: NewCalificacion = {
  calificacion: 42052,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
