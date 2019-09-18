import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Sighting } from './sighting.model';
import { SightingPopupService } from './sighting-popup.service';
import { SightingService } from './sighting.service';

@Component({
    selector: 'jhi-sighting-delete-dialog',
    templateUrl: './sighting-delete-dialog.component.html'
})
export class SightingDeleteDialogComponent {

    sighting: Sighting;

    constructor(
        private sightingService: SightingService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.sightingService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'sightingListModification',
                content: 'Deleted an sighting'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sighting-delete-popup',
    template: ''
})
export class SightingDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private sightingPopupService: SightingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.sightingPopupService
                .open(SightingDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
