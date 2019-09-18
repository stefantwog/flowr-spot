import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FlowrSpotSharedModule } from '../../shared';
import {
    SightingService,
    SightingPopupService,
    SightingComponent,
    SightingDetailComponent,
    SightingDialogComponent,
    SightingPopupComponent,
    SightingDeletePopupComponent,
    SightingDeleteDialogComponent,
    sightingRoute,
    sightingPopupRoute,
    SightingResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...sightingRoute,
    ...sightingPopupRoute,
];

@NgModule({
    imports: [
        FlowrSpotSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SightingComponent,
        SightingDetailComponent,
        SightingDialogComponent,
        SightingDeleteDialogComponent,
        SightingPopupComponent,
        SightingDeletePopupComponent,
    ],
    entryComponents: [
        SightingComponent,
        SightingDialogComponent,
        SightingPopupComponent,
        SightingDeleteDialogComponent,
        SightingDeletePopupComponent,
    ],
    providers: [
        SightingService,
        SightingPopupService,
        SightingResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FlowrSpotSightingModule {}
