/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { FlowrSpotTestModule } from '../../../test.module';
import { SightingDetailComponent } from '../../../../../../main/webapp/app/entities/sighting/sighting-detail.component';
import { SightingService } from '../../../../../../main/webapp/app/entities/sighting/sighting.service';
import { Sighting } from '../../../../../../main/webapp/app/entities/sighting/sighting.model';

describe('Component Tests', () => {

    describe('Sighting Management Detail Component', () => {
        let comp: SightingDetailComponent;
        let fixture: ComponentFixture<SightingDetailComponent>;
        let service: SightingService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FlowrSpotTestModule],
                declarations: [SightingDetailComponent],
                providers: [
                    SightingService
                ]
            })
            .overrideTemplate(SightingDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SightingDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SightingService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Sighting(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.sighting).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
