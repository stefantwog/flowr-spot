import { BaseEntity } from './../../shared';

export class SightingLike implements BaseEntity {
    constructor(
        public id?: number,
        public sighting?: BaseEntity,
    ) {
    }
}
