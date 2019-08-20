import {Entity} from '@app/entity/entity';

export class Contact extends Entity {

    assigner: number | null;
    curators: number[] | null;

    firstName: string | null;
    secondName: string | null;
    thirdName: string | null;
    birthday: Date | null;
}
