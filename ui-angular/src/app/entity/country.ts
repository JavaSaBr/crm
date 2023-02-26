import {NamedUniqEntity} from '@app/entity/named-uniq-entity';

export class Country extends NamedUniqEntity {

    public static copy(another?: Country | null) {
        return new Country(
            another ? another.id : null,
            another ? another.name : null,
            another ? another.name.toLocaleLowerCase() : null,
            another ? another.flagCode : null,
            another ? another.phoneCode : null,
        );
    }

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
