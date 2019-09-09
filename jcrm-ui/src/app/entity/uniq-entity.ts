import {Entity} from '@app/entity/entity';

export class UniqEntity extends Entity {

    id: number | null;

    constructor(id: number | null) {
        super();
        this.id = id;
    }
}
