import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MateriaFormService } from './materia-form.service';
import { MateriaService } from '../service/materia.service';
import { IMateria } from '../materia.model';

import { MateriaUpdateComponent } from './materia-update.component';

describe('Materia Management Update Component', () => {
  let comp: MateriaUpdateComponent;
  let fixture: ComponentFixture<MateriaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let materiaFormService: MateriaFormService;
  let materiaService: MateriaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MateriaUpdateComponent],
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
      .overrideTemplate(MateriaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MateriaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    materiaFormService = TestBed.inject(MateriaFormService);
    materiaService = TestBed.inject(MateriaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const materia: IMateria = { id: 456 };

      activatedRoute.data = of({ materia });
      comp.ngOnInit();

      expect(comp.materia).toEqual(materia);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMateria>>();
      const materia = { id: 123 };
      jest.spyOn(materiaFormService, 'getMateria').mockReturnValue(materia);
      jest.spyOn(materiaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materia }));
      saveSubject.complete();

      // THEN
      expect(materiaFormService.getMateria).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(materiaService.update).toHaveBeenCalledWith(expect.objectContaining(materia));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMateria>>();
      const materia = { id: 123 };
      jest.spyOn(materiaFormService, 'getMateria').mockReturnValue({ id: null });
      jest.spyOn(materiaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materia: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materia }));
      saveSubject.complete();

      // THEN
      expect(materiaFormService.getMateria).toHaveBeenCalled();
      expect(materiaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMateria>>();
      const materia = { id: 123 };
      jest.spyOn(materiaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(materiaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
