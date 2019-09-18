import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Sighting } from './sighting.model';
import { SightingPopupService } from './sighting-popup.service';
import { SightingService } from './sighting.service';
import { Flower, FlowerService } from '../flower';

@Component({
    selector: 'jhi-sighting-dialog',
    templateUrl: './sighting-dialog.component.html'
})
export class SightingDialogComponent implements OnInit {

    sighting: Sighting;
    isSaving: boolean;

    flowers: Flower[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private sightingService: SightingService,
        private flowerService: FlowerService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.flowerService.query()
            .subscribe((res: HttpResponse<Flower[]>) => { this.flowers = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.sighting.id !== undefined) {
            this.subscribeToSaveResponse(
                this.sightingService.update(this.sighting));
        } else {
            this.subscribeToSaveResponse(
                this.sightingService.create(this.sighting));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Sighting>>) {
        result.subscribe((res: HttpResponse<Sighting>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Sighting) {
        this.eventManager.broadcast({ name: 'sightingListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackFlowerById(index: number, item: Flower) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-sighting-popup',
    template: ''
})
export class SightingPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private sightingPopupService: SightingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.sightingPopupService
                    .open(SightingDialogComponent as Component, params['id']);
            } else {
                this.sightingPopupService
                    .open(SightingDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
