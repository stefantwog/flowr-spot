import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SightingLike } from './sighting-like.model';
import { SightingLikePopupService } from './sighting-like-popup.service';
import { SightingLikeService } from './sighting-like.service';

@Component({
    selector: 'jhi-sighting-like-delete-dialog',
    templateUrl: './sighting-like-delete-dialog.component.html'
})
export class SightingLikeDeleteDialogComponent {

    sightingLike: SightingLike;

    constructor(
        private sightingLikeService: SightingLikeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.sightingLikeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'sightingLikeListModification',
                content: 'Deleted an sightingLike'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sighting-like-delete-popup',
    template: ''
})
export class SightingLikeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private sightingLikePopupService: SightingLikePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.sightingLikePopupService
                .open(SightingLikeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
