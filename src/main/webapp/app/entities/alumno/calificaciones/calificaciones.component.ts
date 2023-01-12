import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ASC, DESC, ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICalificacion } from 'app/entities/calificacion/calificacion.model';

import { IAlumno } from '../alumno.model';
import { AlumnoService } from '../service/alumno.service';

import { CalificacionDeleteDialogComponent } from '../../calificacion/delete/calificacion-delete-dialog.component';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { SortService } from 'app/shared/sort/sort.service';
import { CalificacionService } from 'app/entities/calificacion/service/calificacion.service';

@Component({
  selector: 'jhi-calificaciones',
  templateUrl: './calificaciones.component.html',
  styleUrls: ['./calificaciones.component.scss'],
})
export class CalificacionesComponent implements OnInit {
  alumno: IAlumno | null = null;

  predicate = 'id';
  ascending = true;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected calificacionService: CalificacionService,
    protected service: AlumnoService,
    public router: Router,
    protected modalService: NgbModal,
    protected sortService: SortService
  ) {}

  trackId = (_index: number, item: ICalificacion): number => this.calificacionService.getCalificacionIdentifier(item);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ alumno }) => {
      this.alumno = alumno;
      console.log(alumno);
      let id = this.alumno ? this.alumno.id : 0;
      console.log(id);
    });
  }

  previousState(): void {
    window.history.back();
  }
  /*
  delete(calificacion: ICalificacion): void {
    const modalRef = this.modalService.open(CalificacionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.calificacion = calificacion;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations())
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }

  protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend(this.predicate, this.ascending))
    );
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.alumno.calificacion = this.refineData(dataFromBody);
  }

  protected refineData(data: ICalificacion[]): ICalificacion[] {
    return data.sort(this.sortService.startSort(this.predicate, this.ascending ? 1 : -1));
  }

  protected fillComponentAttributesFromResponseBody(data: ICalificacion[] | null): ICalificacion[] {
    return data ?? [];
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.predicate, this.ascending);
  }

  protected handleNavigation(predicate?: string, ascending?: boolean): void {
    const queryParamsObj = {
      sort: this.getSortQueryParam(predicate, ascending),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }
  */
}
