import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { SightingComponent } from './sighting.component';
import { SightingDetailComponent } from './sighting-detail.component';
import { SightingPopupComponent } from './sighting-dialog.component';
import { SightingDeletePopupComponent } from './sighting-delete-dialog.component';

@Injectable()
export class SightingResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const sightingRoute: Routes = [
    {
        path: 'sighting',
        component: SightingComponent,
        resolve: {
            'pagingParams': SightingResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'flowrSpotApp.sighting.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'sighting/:id',
        component: SightingDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'flowrSpotApp.sighting.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const sightingPopupRoute: Routes = [
    {
        path: 'sighting-new',
        component: SightingPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'flowrSpotApp.sighting.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sighting/:id/edit',
        component: SightingPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'flowrSpotApp.sighting.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sighting/:id/delete',
        component: SightingDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'flowrSpotApp.sighting.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
