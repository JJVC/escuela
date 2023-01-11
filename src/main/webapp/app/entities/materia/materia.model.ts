export interface IMateria {
  id: number;
  nombre?: string | null;
  activo?: boolean | null;
}

export type NewMateria = Omit<IMateria, 'id'> & { id: null };
