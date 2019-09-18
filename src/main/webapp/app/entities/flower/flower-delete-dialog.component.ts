import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Flower } from './flower.model';
import { FlowerPopupService } from './flower-popup.service';
import { FlowerService } from './flower.service';

@Component({
    selector: 'jhi-flower-delete-dialog',
    templateUrl: './flower-delete-dialog.component.html'
})
export class FlowerDeleteDialogComponent {

    flower: Flower;

    constructor(
        private flowerService: FlowerService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.flowerService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'flowerListModification',
                content: 'Deleted an flower'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-flower-delete-popup',
    template: ''
})
export class FlowerDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private flowerPopupService: FlowerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.flowerPopupService
                .open(FlowerDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
