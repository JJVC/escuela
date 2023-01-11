import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AlumnoFormService } from './alumno-form.service';
import { AlumnoService } from '../service/alumno.service';
import { IAlumno } from '../alumno.model';

import { AlumnoUpdateComponent } from './alumno-update.component';

describe('Alumno Management Update Component', () => {
  let comp: AlumnoUpdateComponent;
  let fixture: ComponentFixture<AlumnoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let alumnoFormService: AlumnoFormService;
  let alumnoService: AlumnoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AlumnoUpdateComponent],
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
      .overrideTemplate(AlumnoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AlumnoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    alumnoFormService = TestBed.inject(AlumnoFormService);
    alumnoService = TestBed.inject(AlumnoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const alumno: IAlumno = { id: 456 };

      activatedRoute.data = of({ alumno });
      comp.ngOnInit();

      expect(comp.alumno).toEqual(alumno);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlumno>>();
      const alumno = { id: 123 };
      jest.spyOn(alumnoFormService, 'getAlumno').mockReturnValue(alumno);
      jest.spyOn(alumnoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alumno });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alumno }));
      saveSubject.complete();

      // THEN
      expect(alumnoFormService.getAlumno).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(alumnoService.update).toHaveBeenCalledWith(expect.objectContaining(alumno));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlumno>>();
      const alumno = { id: 123 };
      jest.spyOn(alumnoFormService, 'getAlumno').mockReturnValue({ id: null });
      jest.spyOn(alumnoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alumno: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alumno }));
      saveSubject.complete();

      // THEN
      expect(alumnoFormService.getAlumno).toHaveBeenCalled();
      expect(alumnoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlumno>>();
      const alumno = { id: 123 };
      jest.spyOn(alumnoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alumno });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(alumnoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
