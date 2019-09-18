import { BaseEntity } from './../../shared';

export class Flower implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public image?: string,
        public description?: any,
        public sightings?: BaseEntity[],
    ) {
    }
}
