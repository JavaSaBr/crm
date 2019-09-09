import {UniqEntity} from '@app/entity/uniq-entity';

export class NamedUniqEntity extends UniqEntity {

    name: string | null;
    nameInLowerCase: string | null;
}
