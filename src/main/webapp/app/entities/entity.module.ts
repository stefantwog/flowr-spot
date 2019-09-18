import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { FlowrSpotFlowerModule } from './flower/flower.module';
import { FlowrSpotSightingModule } from './sighting/sighting.module';
import { FlowrSpotSightingLikeModule } from './sighting-like/sighting-like.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        FlowrSpotFlowerModule,
        FlowrSpotSightingModule,
        FlowrSpotSightingLikeModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FlowrSpotEntityModule {}
