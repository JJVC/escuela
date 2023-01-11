import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICalificacion, NewCalificacion } from '../calificacion.model';

export type PartialUpdateCalificacion = Partial<ICalificacion> & Pick<ICalificacion, 'id'>;

type RestOf<T extends ICalificacion | NewCalificacion> = Omit<T, 'fechaRegistro'> & {
  fechaRegistro?: string | null;
};

export type RestCalificacion = RestOf<ICalificacion>;

export type NewRestCalificacion = RestOf<NewCalificacion>;

export type PartialUpdateRestCalificacion = RestOf<PartialUpdateCalificacion>;

export type EntityResponseType = HttpResponse<ICalificacion>;
export type EntityArrayResponseType = HttpResponse<ICalificacion[]>;

@Injectable({ providedIn: 'root' })
export class CalificacionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/calificacions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(calificacion: NewCalificacion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(calificacion);
    return this.http
      .post<RestCalificacion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(calificacion: ICalificacion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(calificacion);
    return this.http
      .put<RestCalificacion>(`${this.resourceUrl}/${this.getCalificacionIdentifier(calificacion)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(calificacion: PartialUpdateCalificacion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(calificacion);
    return this.http
      .patch<RestCalificacion>(`${this.resourceUrl}/${this.getCalificacionIdentifier(calificacion)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCalificacion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCalificacion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCalificacionIdentifier(calificacion: Pick<ICalificacion, 'id'>): number {
    return calificacion.id;
  }

  compareCalificacion(o1: Pick<ICalificacion, 'id'> | null, o2: Pick<ICalificacion, 'id'> | null): boolean {
    return o1 && o2 ? this.getCalificacionIdentifier(o1) === this.getCalificacionIdentifier(o2) : o1 === o2;
  }

  addCalificacionToCollectionIfMissing<Type extends Pick<ICalificacion, 'id'>>(
    calificacionCollection: Type[],
    ...calificacionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const calificacions: Type[] = calificacionsToCheck.filter(isPresent);
    if (calificacions.length > 0) {
      const calificacionCollectionIdentifiers = calificacionCollection.map(
        calificacionItem => this.getCalificacionIdentifier(calificacionItem)!
      );
      const calificacionsToAdd = calificacions.filter(calificacionItem => {
        const calificacionIdentifier = this.getCalificacionIdentifier(calificacionItem);
        if (calificacionCollectionIdentifiers.includes(calificacionIdentifier)) {
          return false;
        }
        calificacionCollectionIdentifiers.push(calificacionIdentifier);
        return true;
      });
      return [...calificacionsToAdd, ...calificacionCollection];
    }
    return calificacionCollection;
  }

  protected convertDateFromClient<T extends ICalificacion | NewCalificacion | PartialUpdateCalificacion>(calificacion: T): RestOf<T> {
    return {
      ...calificacion,
      fechaRegistro: calificacion.fechaRegistro?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCalificacion: RestCalificacion): ICalificacion {
    return {
      ...restCalificacion,
      fechaRegistro: restCalificacion.fechaRegistro ? dayjs(restCalificacion.fechaRegistro) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCalificacion>): HttpResponse<ICalificacion> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCalificacion[]>): HttpResponse<ICalificacion[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
