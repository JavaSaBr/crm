import {UniqEntity} from '@app/entity/uniq-entity';

export class NamedUniqEntity extends UniqEntity {

    constructor(id: number | null, public name: string | null, public nameInLowerCase: string | null) {
        super(id);
    }
}
