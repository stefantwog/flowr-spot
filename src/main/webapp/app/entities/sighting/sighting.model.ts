import { BaseEntity } from './../../shared';

export class Sighting implements BaseEntity {
    constructor(
        public id?: number,
        public longitude?: number,
        public latitude?: number,
        public image?: string,
        public likes?: BaseEntity[],
        public flower?: BaseEntity,
    ) {
    }
}
