import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { FlowerComponent } from './flower.component';
import { FlowerDetailComponent } from './flower-detail.component';
import { FlowerPopupComponent } from './flower-dialog.component';
import { FlowerDeletePopupComponent } from './flower-delete-dialog.component';

@Injectable()
export class FlowerResolvePagingParams implements Resolve<any> {

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

export const flowerRoute: Routes = [
    {
        path: 'flower',
        component: FlowerComponent,
        resolve: {
            'pagingParams': FlowerResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'flowrSpotApp.flower.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'flower/:id',
        component: FlowerDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'flowrSpotApp.flower.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const flowerPopupRoute: Routes = [
    {
        path: 'flower-new',
        component: FlowerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'flowrSpotApp.flower.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'flower/:id/edit',
        component: FlowerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'flowrSpotApp.flower.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'flower/:id/delete',
        component: FlowerDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'flowrSpotApp.flower.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
