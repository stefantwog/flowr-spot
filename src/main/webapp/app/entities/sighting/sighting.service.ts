import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Sighting } from './sighting.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Sighting>;

@Injectable()
export class SightingService {

    private resourceUrl =  SERVER_API_URL + 'api/sightings';

    constructor(private http: HttpClient) { }

    create(sighting: Sighting): Observable<EntityResponseType> {
        const copy = this.convert(sighting);
        return this.http.post<Sighting>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(sighting: Sighting): Observable<EntityResponseType> {
        const copy = this.convert(sighting);
        return this.http.put<Sighting>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Sighting>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Sighting[]>> {
        const options = createRequestOption(req);
        return this.http.get<Sighting[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Sighting[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Sighting = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Sighting[]>): HttpResponse<Sighting[]> {
        const jsonResponse: Sighting[] = res.body;
        const body: Sighting[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Sighting.
     */
    private convertItemFromServer(sighting: Sighting): Sighting {
        const copy: Sighting = Object.assign({}, sighting);
        return copy;
    }

    /**
     * Convert a Sighting to a JSON which can be sent to the server.
     */
    private convert(sighting: Sighting): Sighting {
        const copy: Sighting = Object.assign({}, sighting);
        return copy;
    }
}
