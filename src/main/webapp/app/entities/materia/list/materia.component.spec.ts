import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MateriaService } from '../service/materia.service';

import { MateriaComponent } from './materia.component';

describe('Materia Management Component', () => {
  let comp: MateriaComponent;
  let fixture: ComponentFixture<MateriaComponent>;
  let service: MateriaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'materia', component: MateriaComponent }]), HttpClientTestingModule],
      declarations: [MateriaComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(MateriaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MateriaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MateriaService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.materias?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to materiaService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getMateriaIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getMateriaIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
