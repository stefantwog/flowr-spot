import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FlowrSpotSharedModule } from '../../shared';
import {
    FlowerService,
    FlowerPopupService,
    FlowerComponent,
    FlowerDetailComponent,
    FlowerDialogComponent,
    FlowerPopupComponent,
    FlowerDeletePopupComponent,
    FlowerDeleteDialogComponent,
    flowerRoute,
    flowerPopupRoute,
    FlowerResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...flowerRoute,
    ...flowerPopupRoute,
];

@NgModule({
    imports: [
        FlowrSpotSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        FlowerComponent,
        FlowerDetailComponent,
        FlowerDialogComponent,
        FlowerDeleteDialogComponent,
        FlowerPopupComponent,
        FlowerDeletePopupComponent,
    ],
    entryComponents: [
        FlowerComponent,
        FlowerDialogComponent,
        FlowerPopupComponent,
        FlowerDeleteDialogComponent,
        FlowerDeletePopupComponent,
    ],
    providers: [
        FlowerService,
        FlowerPopupService,
        FlowerResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FlowrSpotFlowerModule {}
