import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { Flower } from './flower.model';
import { FlowerPopupService } from './flower-popup.service';
import { FlowerService } from './flower.service';

@Component({
    selector: 'jhi-flower-dialog',
    templateUrl: './flower-dialog.component.html'
})
export class FlowerDialogComponent implements OnInit {

    flower: Flower;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private flowerService: FlowerService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.flower.id !== undefined) {
            this.subscribeToSaveResponse(
                this.flowerService.update(this.flower));
        } else {
            this.subscribeToSaveResponse(
                this.flowerService.create(this.flower));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Flower>>) {
        result.subscribe((res: HttpResponse<Flower>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Flower) {
        this.eventManager.broadcast({ name: 'flowerListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-flower-popup',
    template: ''
})
export class FlowerPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private flowerPopupService: FlowerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.flowerPopupService
                    .open(FlowerDialogComponent as Component, params['id']);
            } else {
                this.flowerPopupService
                    .open(FlowerDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
