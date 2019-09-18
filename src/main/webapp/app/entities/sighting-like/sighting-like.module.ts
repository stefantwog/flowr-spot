import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FlowrSpotSharedModule } from '../../shared';
import {
    SightingLikeService,
    SightingLikePopupService,
    SightingLikeComponent,
    SightingLikeDetailComponent,
    SightingLikeDialogComponent,
    SightingLikePopupComponent,
    SightingLikeDeletePopupComponent,
    SightingLikeDeleteDialogComponent,
    sightingLikeRoute,
    sightingLikePopupRoute,
    SightingLikeResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...sightingLikeRoute,
    ...sightingLikePopupRoute,
];

@NgModule({
    imports: [
        FlowrSpotSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SightingLikeComponent,
        SightingLikeDetailComponent,
        SightingLikeDialogComponent,
        SightingLikeDeleteDialogComponent,
        SightingLikePopupComponent,
        SightingLikeDeletePopupComponent,
    ],
    entryComponents: [
        SightingLikeComponent,
        SightingLikeDialogComponent,
        SightingLikePopupComponent,
        SightingLikeDeleteDialogComponent,
        SightingLikeDeletePopupComponent,
    ],
    providers: [
        SightingLikeService,
        SightingLikePopupService,
        SightingLikeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FlowrSpotSightingLikeModule {}
