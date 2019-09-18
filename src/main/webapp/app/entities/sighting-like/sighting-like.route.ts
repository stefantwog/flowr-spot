import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { SightingLikeComponent } from './sighting-like.component';
import { SightingLikeDetailComponent } from './sighting-like-detail.component';
import { SightingLikePopupComponent } from './sighting-like-dialog.component';
import { SightingLikeDeletePopupComponent } from './sighting-like-delete-dialog.component';

@Injectable()
export class SightingLikeResolvePagingParams implements Resolve<any> {

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

export const sightingLikeRoute: Routes = [
    {
        path: 'sighting-like',
        component: SightingLikeComponent,
        resolve: {
            'pagingParams': SightingLikeResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'flowrSpotApp.sightingLike.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'sighting-like/:id',
        component: SightingLikeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'flowrSpotApp.sightingLike.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const sightingLikePopupRoute: Routes = [
    {
        path: 'sighting-like-new',
        component: SightingLikePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'flowrSpotApp.sightingLike.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sighting-like/:id/edit',
        component: SightingLikePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'flowrSpotApp.sightingLike.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sighting-like/:id/delete',
        component: SightingLikeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'flowrSpotApp.sightingLike.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
