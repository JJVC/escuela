import { IMateria, NewMateria } from './materia.model';

export const sampleWithRequiredData: IMateria = {
  id: 91727,
  nombre: 'global Guapa Inteligente',
};

export const sampleWithPartialData: IMateria = {
  id: 12398,
  nombre: 'connecting Queso e-markets',
  activo: false,
};

export const sampleWithFullData: IMateria = {
  id: 55798,
  nombre: 'Loan Silla',
  activo: true,
};

export const sampleWithNewData: NewMateria = {
  nombre: 'Teclado SQL Subida',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
