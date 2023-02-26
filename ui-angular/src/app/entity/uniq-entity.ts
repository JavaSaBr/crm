import {Entity} from '@app/entity/entity';

export class UniqEntity extends Entity {

    constructor(public id: number | null) {
        super();
    }
}
