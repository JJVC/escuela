import { IAlumno, NewAlumno } from './alumno.model';

export const sampleWithRequiredData: IAlumno = {
  id: 48853,
  nombre: 'Asistente',
  apPaterno: 'Pequeño',
};

export const sampleWithPartialData: IAlumno = {
  id: 33730,
  nombre: 'Madera',
  apPaterno: 'SCSI',
  activo: true,
};

export const sampleWithFullData: IAlumno = {
  id: 74953,
  nombre: 'metrics Inversor Avon',
  apPaterno: 'bypassing',
  apMaterno: 'explícita',
  display: 'Hormigon granular Contabilidad',
  activo: false,
};

export const sampleWithNewData: NewAlumno = {
  nombre: 'Salchichas',
  apPaterno: 'Chalet Sabroso Automatizado',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
