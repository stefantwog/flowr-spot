import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { SightingLike } from './sighting-like.model';
import { SightingLikeService } from './sighting-like.service';

@Component({
    selector: 'jhi-sighting-like-detail',
    templateUrl: './sighting-like-detail.component.html'
})
export class SightingLikeDetailComponent implements OnInit, OnDestroy {

    sightingLike: SightingLike;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private sightingLikeService: SightingLikeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSightingLikes();
    }

    load(id) {
        this.sightingLikeService.find(id)
            .subscribe((sightingLikeResponse: HttpResponse<SightingLike>) => {
                this.sightingLike = sightingLikeResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSightingLikes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'sightingLikeListModification',
            (response) => this.load(this.sightingLike.id)
        );
    }
}
