import {Entity} from '@app/entity/entity';

export class User extends Entity {

    email: string | null;
    firstName: string | null;
    secondName: string | null;
    phoneNumber: string | null;
}
