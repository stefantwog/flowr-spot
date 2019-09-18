/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FlowrSpotTestModule } from '../../../test.module';
import { SightingComponent } from '../../../../../../main/webapp/app/entities/sighting/sighting.component';
import { SightingService } from '../../../../../../main/webapp/app/entities/sighting/sighting.service';
import { Sighting } from '../../../../../../main/webapp/app/entities/sighting/sighting.model';

describe('Component Tests', () => {

    describe('Sighting Management Component', () => {
        let comp: SightingComponent;
        let fixture: ComponentFixture<SightingComponent>;
        let service: SightingService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FlowrSpotTestModule],
                declarations: [SightingComponent],
                providers: [
                    SightingService
                ]
            })
            .overrideTemplate(SightingComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SightingComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SightingService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Sighting(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.sightings[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
