import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SightingLike } from './sighting-like.model';
import { SightingLikePopupService } from './sighting-like-popup.service';
import { SightingLikeService } from './sighting-like.service';
import { Sighting, SightingService } from '../sighting';

@Component({
    selector: 'jhi-sighting-like-dialog',
    templateUrl: './sighting-like-dialog.component.html'
})
export class SightingLikeDialogComponent implements OnInit {

    sightingLike: SightingLike;
    isSaving: boolean;

    sightings: Sighting[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private sightingLikeService: SightingLikeService,
        private sightingService: SightingService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.sightingService.query()
            .subscribe((res: HttpResponse<Sighting[]>) => { this.sightings = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.sightingLike.id !== undefined) {
            this.subscribeToSaveResponse(
                this.sightingLikeService.update(this.sightingLike));
        } else {
            this.subscribeToSaveResponse(
                this.sightingLikeService.create(this.sightingLike));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<SightingLike>>) {
        result.subscribe((res: HttpResponse<SightingLike>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: SightingLike) {
        this.eventManager.broadcast({ name: 'sightingLikeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackSightingById(index: number, item: Sighting) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-sighting-like-popup',
    template: ''
})
export class SightingLikePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private sightingLikePopupService: SightingLikePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.sightingLikePopupService
                    .open(SightingLikeDialogComponent as Component, params['id']);
            } else {
                this.sightingLikePopupService
                    .open(SightingLikeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
