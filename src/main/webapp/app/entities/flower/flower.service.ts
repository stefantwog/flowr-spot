import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Flower } from './flower.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Flower>;

@Injectable()
export class FlowerService {

    private resourceUrl =  SERVER_API_URL + 'api/flowers';

    constructor(private http: HttpClient) { }

    create(flower: Flower): Observable<EntityResponseType> {
        const copy = this.convert(flower);
        return this.http.post<Flower>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(flower: Flower): Observable<EntityResponseType> {
        const copy = this.convert(flower);
        return this.http.put<Flower>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Flower>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Flower[]>> {
        const options = createRequestOption(req);
        return this.http.get<Flower[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Flower[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Flower = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Flower[]>): HttpResponse<Flower[]> {
        const jsonResponse: Flower[] = res.body;
        const body: Flower[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Flower.
     */
    private convertItemFromServer(flower: Flower): Flower {
        const copy: Flower = Object.assign({}, flower);
        return copy;
    }

    /**
     * Convert a Flower to a JSON which can be sent to the server.
     */
    private convert(flower: Flower): Flower {
        const copy: Flower = Object.assign({}, flower);
        return copy;
    }
}
