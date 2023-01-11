import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CalificacionFormService } from './calificacion-form.service';
import { CalificacionService } from '../service/calificacion.service';
import { ICalificacion } from '../calificacion.model';
import { IMateria } from 'app/entities/materia/materia.model';
import { MateriaService } from 'app/entities/materia/service/materia.service';
import { IAlumno } from 'app/entities/alumno/alumno.model';
import { AlumnoService } from 'app/entities/alumno/service/alumno.service';

import { CalificacionUpdateComponent } from './calificacion-update.component';

describe('Calificacion Management Update Component', () => {
  let comp: CalificacionUpdateComponent;
  let fixture: ComponentFixture<CalificacionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let calificacionFormService: CalificacionFormService;
  let calificacionService: CalificacionService;
  let materiaService: MateriaService;
  let alumnoService: AlumnoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CalificacionUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CalificacionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CalificacionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    calificacionFormService = TestBed.inject(CalificacionFormService);
    calificacionService = TestBed.inject(CalificacionService);
    materiaService = TestBed.inject(MateriaService);
    alumnoService = TestBed.inject(AlumnoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Materia query and add missing value', () => {
      const calificacion: ICalificacion = { id: 456 };
      const materia: IMateria = { id: 56516 };
      calificacion.materia = materia;

      const materiaCollection: IMateria[] = [{ id: 63651 }];
      jest.spyOn(materiaService, 'query').mockReturnValue(of(new HttpResponse({ body: materiaCollection })));
      const additionalMaterias = [materia];
      const expectedCollection: IMateria[] = [...additionalMaterias, ...materiaCollection];
      jest.spyOn(materiaService, 'addMateriaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ calificacion });
      comp.ngOnInit();

      expect(materiaService.query).toHaveBeenCalled();
      expect(materiaService.addMateriaToCollectionIfMissing).toHaveBeenCalledWith(
        materiaCollection,
        ...additionalMaterias.map(expect.objectContaining)
      );
      expect(comp.materiasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Alumno query and add missing value', () => {
      const calificacion: ICalificacion = { id: 456 };
      const alumno: IAlumno = { id: 64330 };
      calificacion.alumno = alumno;

      const alumnoCollection: IAlumno[] = [{ id: 50649 }];
      jest.spyOn(alumnoService, 'query').mockReturnValue(of(new HttpResponse({ body: alumnoCollection })));
      const additionalAlumnos = [alumno];
      const expectedCollection: IAlumno[] = [...additionalAlumnos, ...alumnoCollection];
      jest.spyOn(alumnoService, 'addAlumnoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ calificacion });
      comp.ngOnInit();

      expect(alumnoService.query).toHaveBeenCalled();
      expect(alumnoService.addAlumnoToCollectionIfMissing).toHaveBeenCalledWith(
        alumnoCollection,
        ...additionalAlumnos.map(expect.objectContaining)
      );
      expect(comp.alumnosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const calificacion: ICalificacion = { id: 456 };
      const materia: IMateria = { id: 39950 };
      calificacion.materia = materia;
      const alumno: IAlumno = { id: 20831 };
      calificacion.alumno = alumno;

      activatedRoute.data = of({ calificacion });
      comp.ngOnInit();

      expect(comp.materiasSharedCollection).toContain(materia);
      expect(comp.alumnosSharedCollection).toContain(alumno);
      expect(comp.calificacion).toEqual(calificacion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICalificacion>>();
      const calificacion = { id: 123 };
      jest.spyOn(calificacionFormService, 'getCalificacion').mockReturnValue(calificacion);
      jest.spyOn(calificacionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ calificacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: calificacion }));
      saveSubject.complete();

      // THEN
      expect(calificacionFormService.getCalificacion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(calificacionService.update).toHaveBeenCalledWith(expect.objectContaining(calificacion));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICalificacion>>();
      const calificacion = { id: 123 };
      jest.spyOn(calificacionFormService, 'getCalificacion').mockReturnValue({ id: null });
      jest.spyOn(calificacionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ calificacion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: calificacion }));
      saveSubject.complete();

      // THEN
      expect(calificacionFormService.getCalificacion).toHaveBeenCalled();
      expect(calificacionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICalificacion>>();
      const calificacion = { id: 123 };
      jest.spyOn(calificacionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ calificacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(calificacionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMateria', () => {
      it('Should forward to materiaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(materiaService, 'compareMateria');
        comp.compareMateria(entity, entity2);
        expect(materiaService.compareMateria).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAlumno', () => {
      it('Should forward to alumnoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(alumnoService, 'compareAlumno');
        comp.compareAlumno(entity, entity2);
        expect(alumnoService.compareAlumno).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
