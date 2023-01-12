import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAlumno, NewAlumno } from '../alumno.model';

export type PartialUpdateAlumno = Partial<IAlumno> & Pick<IAlumno, 'id'>;

export type EntityResponseType = HttpResponse<IAlumno>;
export type EntityArrayResponseType = HttpResponse<IAlumno[]>;

@Injectable({ providedIn: 'root' })
export class AlumnoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/alumnos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(alumno: NewAlumno): Observable<EntityResponseType> {
    return this.http.post<IAlumno>(this.resourceUrl, alumno, { observe: 'response' });
  }

  update(alumno: IAlumno): Observable<EntityResponseType> {
    return this.http.put<IAlumno>(`${this.resourceUrl}/${this.getAlumnoIdentifier(alumno)}`, alumno, { observe: 'response' });
  }

  partialUpdate(alumno: PartialUpdateAlumno): Observable<EntityResponseType> {
    return this.http.patch<IAlumno>(`${this.resourceUrl}/${this.getAlumnoIdentifier(alumno)}`, alumno, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAlumno>(`${this.resourceUrl}/${id}/calificaciones`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAlumno[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAlumnoIdentifier(alumno: Pick<IAlumno, 'id'>): number {
    return alumno.id;
  }

  compareAlumno(o1: Pick<IAlumno, 'id'> | null, o2: Pick<IAlumno, 'id'> | null): boolean {
    return o1 && o2 ? this.getAlumnoIdentifier(o1) === this.getAlumnoIdentifier(o2) : o1 === o2;
  }

  addAlumnoToCollectionIfMissing<Type extends Pick<IAlumno, 'id'>>(
    alumnoCollection: Type[],
    ...alumnosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const alumnos: Type[] = alumnosToCheck.filter(isPresent);
    if (alumnos.length > 0) {
      const alumnoCollectionIdentifiers = alumnoCollection.map(alumnoItem => this.getAlumnoIdentifier(alumnoItem)!);
      const alumnosToAdd = alumnos.filter(alumnoItem => {
        const alumnoIdentifier = this.getAlumnoIdentifier(alumnoItem);
        if (alumnoCollectionIdentifiers.includes(alumnoIdentifier)) {
          return false;
        }
        alumnoCollectionIdentifiers.push(alumnoIdentifier);
        return true;
      });
      return [...alumnosToAdd, ...alumnoCollection];
    }
    return alumnoCollection;
  }
}
