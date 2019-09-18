/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { FlowrSpotTestModule } from '../../../test.module';
import { SightingLikeDialogComponent } from '../../../../../../main/webapp/app/entities/sighting-like/sighting-like-dialog.component';
import { SightingLikeService } from '../../../../../../main/webapp/app/entities/sighting-like/sighting-like.service';
import { SightingLike } from '../../../../../../main/webapp/app/entities/sighting-like/sighting-like.model';
import { SightingService } from '../../../../../../main/webapp/app/entities/sighting';

describe('Component Tests', () => {

    describe('SightingLike Management Dialog Component', () => {
        let comp: SightingLikeDialogComponent;
        let fixture: ComponentFixture<SightingLikeDialogComponent>;
        let service: SightingLikeService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FlowrSpotTestModule],
                declarations: [SightingLikeDialogComponent],
                providers: [
                    SightingService,
                    SightingLikeService
                ]
            })
            .overrideTemplate(SightingLikeDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SightingLikeDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SightingLikeService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new SightingLike(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.sightingLike = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'sightingLikeListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new SightingLike();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.sightingLike = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'sightingLikeListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
