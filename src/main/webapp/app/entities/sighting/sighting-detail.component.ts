import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Sighting } from './sighting.model';
import { SightingService } from './sighting.service';

@Component({
    selector: 'jhi-sighting-detail',
    templateUrl: './sighting-detail.component.html'
})
export class SightingDetailComponent implements OnInit, OnDestroy {

    sighting: Sighting;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private sightingService: SightingService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSightings();
    }

    load(id) {
        this.sightingService.find(id)
            .subscribe((sightingResponse: HttpResponse<Sighting>) => {
                this.sighting = sightingResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSightings() {
        this.eventSubscriber = this.eventManager.subscribe(
            'sightingListModification',
            (response) => this.load(this.sighting.id)
        );
    }
}
