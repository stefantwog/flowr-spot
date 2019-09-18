import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { Flower } from './flower.model';
import { FlowerService } from './flower.service';

@Component({
    selector: 'jhi-flower-detail',
    templateUrl: './flower-detail.component.html'
})
export class FlowerDetailComponent implements OnInit, OnDestroy {

    flower: Flower;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private flowerService: FlowerService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInFlowers();
    }

    load(id) {
        this.flowerService.find(id)
            .subscribe((flowerResponse: HttpResponse<Flower>) => {
                this.flower = flowerResponse.body;
            });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInFlowers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'flowerListModification',
            (response) => this.load(this.flower.id)
        );
    }
}
