import {NamedUniqEntity} from '@app/entity/named-uniq-entity';

export class Country extends NamedUniqEntity {

    flagCode: string | null;
    phoneCode: string | null;
}
