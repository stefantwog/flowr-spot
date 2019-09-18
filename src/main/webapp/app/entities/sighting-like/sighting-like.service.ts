import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { SightingLike } from './sighting-like.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<SightingLike>;

@Injectable()
export class SightingLikeService {

    private resourceUrl =  SERVER_API_URL + 'api/sighting-likes';

    constructor(private http: HttpClient) { }

    create(sightingLike: SightingLike): Observable<EntityResponseType> {
        const copy = this.convert(sightingLike);
        return this.http.post<SightingLike>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(sightingLike: SightingLike): Observable<EntityResponseType> {
        const copy = this.convert(sightingLike);
        return this.http.put<SightingLike>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<SightingLike>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<SightingLike[]>> {
        const options = createRequestOption(req);
        return this.http.get<SightingLike[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<SightingLike[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: SightingLike = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<SightingLike[]>): HttpResponse<SightingLike[]> {
        const jsonResponse: SightingLike[] = res.body;
        const body: SightingLike[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to SightingLike.
     */
    private convertItemFromServer(sightingLike: SightingLike): SightingLike {
        const copy: SightingLike = Object.assign({}, sightingLike);
        return copy;
    }

    /**
     * Convert a SightingLike to a JSON which can be sent to the server.
     */
    private convert(sightingLike: SightingLike): SightingLike {
        const copy: SightingLike = Object.assign({}, sightingLike);
        return copy;
    }
}
