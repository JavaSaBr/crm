import {NamedUniqEntity} from '@app/entity/named-uniq-entity';

export class Country extends NamedUniqEntity {

    constructor(
        id: number | null,
        name: string | null,
        nameInLowerCase: string | null,
        public flagCode: string | null,
        public phoneCode: string | null
    ) {
        super(id, name, nameInLowerCase);
    }
}
