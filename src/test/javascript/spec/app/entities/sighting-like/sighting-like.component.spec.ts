/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FlowrSpotTestModule } from '../../../test.module';
import { SightingLikeComponent } from '../../../../../../main/webapp/app/entities/sighting-like/sighting-like.component';
import { SightingLikeService } from '../../../../../../main/webapp/app/entities/sighting-like/sighting-like.service';
import { SightingLike } from '../../../../../../main/webapp/app/entities/sighting-like/sighting-like.model';

describe('Component Tests', () => {

    describe('SightingLike Management Component', () => {
        let comp: SightingLikeComponent;
        let fixture: ComponentFixture<SightingLikeComponent>;
        let service: SightingLikeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FlowrSpotTestModule],
                declarations: [SightingLikeComponent],
                providers: [
                    SightingLikeService
                ]
            })
            .overrideTemplate(SightingLikeComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SightingLikeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SightingLikeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new SightingLike(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.sightingLikes[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
