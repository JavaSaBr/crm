import {Entity} from '@app/entity/entity';

export class EntityPage<T extends Entity> {

    constructor(
        public entities: T[],
        public totalSize: number
    ) {
    }
}
