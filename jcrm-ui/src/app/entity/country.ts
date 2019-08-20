import {NamedEntity} from '@app/entity/named.entity';

export class Country extends NamedEntity {

    flagCode: string | null;
    phoneCode: string | null;
}
