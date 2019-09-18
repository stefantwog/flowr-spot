/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { FlowrSpotTestModule } from '../../../test.module';
import { SightingLikeDetailComponent } from '../../../../../../main/webapp/app/entities/sighting-like/sighting-like-detail.component';
import { SightingLikeService } from '../../../../../../main/webapp/app/entities/sighting-like/sighting-like.service';
import { SightingLike } from '../../../../../../main/webapp/app/entities/sighting-like/sighting-like.model';

describe('Component Tests', () => {

    describe('SightingLike Management Detail Component', () => {
        let comp: SightingLikeDetailComponent;
        let fixture: ComponentFixture<SightingLikeDetailComponent>;
        let service: SightingLikeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FlowrSpotTestModule],
                declarations: [SightingLikeDetailComponent],
                providers: [
                    SightingLikeService
                ]
            })
            .overrideTemplate(SightingLikeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SightingLikeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SightingLikeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new SightingLike(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.sightingLike).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
