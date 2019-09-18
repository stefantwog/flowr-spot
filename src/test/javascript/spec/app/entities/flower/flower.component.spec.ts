/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FlowrSpotTestModule } from '../../../test.module';
import { FlowerComponent } from '../../../../../../main/webapp/app/entities/flower/flower.component';
import { FlowerService } from '../../../../../../main/webapp/app/entities/flower/flower.service';
import { Flower } from '../../../../../../main/webapp/app/entities/flower/flower.model';

describe('Component Tests', () => {

    describe('Flower Management Component', () => {
        let comp: FlowerComponent;
        let fixture: ComponentFixture<FlowerComponent>;
        let service: FlowerService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FlowrSpotTestModule],
                declarations: [FlowerComponent],
                providers: [
                    FlowerService
                ]
            })
            .overrideTemplate(FlowerComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FlowerComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FlowerService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Flower(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.flowers[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
