/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { FlowrSpotTestModule } from '../../../test.module';
import { FlowerDetailComponent } from '../../../../../../main/webapp/app/entities/flower/flower-detail.component';
import { FlowerService } from '../../../../../../main/webapp/app/entities/flower/flower.service';
import { Flower } from '../../../../../../main/webapp/app/entities/flower/flower.model';

describe('Component Tests', () => {

    describe('Flower Management Detail Component', () => {
        let comp: FlowerDetailComponent;
        let fixture: ComponentFixture<FlowerDetailComponent>;
        let service: FlowerService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FlowrSpotTestModule],
                declarations: [FlowerDetailComponent],
                providers: [
                    FlowerService
                ]
            })
            .overrideTemplate(FlowerDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FlowerDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FlowerService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Flower(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.flower).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
