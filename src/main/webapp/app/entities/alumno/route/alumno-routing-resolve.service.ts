import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAlumno } from '../alumno.model';
import { AlumnoService } from '../service/alumno.service';

@Injectable({ providedIn: 'root' })
export class AlumnoRoutingResolveService implements Resolve<IAlumno | null> {
  constructor(protected service: AlumnoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAlumno | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((alumno: HttpResponse<IAlumno>) => {
          if (alumno.body) {
            return of(alumno.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
