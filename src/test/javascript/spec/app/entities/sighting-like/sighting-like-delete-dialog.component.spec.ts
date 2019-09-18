/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { FlowrSpotTestModule } from '../../../test.module';
import { SightingLikeDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/sighting-like/sighting-like-delete-dialog.component';
import { SightingLikeService } from '../../../../../../main/webapp/app/entities/sighting-like/sighting-like.service';

describe('Component Tests', () => {

    describe('SightingLike Management Delete Component', () => {
        let comp: SightingLikeDeleteDialogComponent;
        let fixture: ComponentFixture<SightingLikeDeleteDialogComponent>;
        let service: SightingLikeService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FlowrSpotTestModule],
                declarations: [SightingLikeDeleteDialogComponent],
                providers: [
                    SightingLikeService
                ]
            })
            .overrideTemplate(SightingLikeDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SightingLikeDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SightingLikeService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
