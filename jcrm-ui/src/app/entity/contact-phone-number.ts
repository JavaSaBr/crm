import {Entity} from '@app/entity/entity';

export enum PhoneNumberType {
    WORK = 'work',
    MOBILE = 'mobile'
}

export class ContactPhoneNumber extends Entity {

    phoneNumber: string | null;
    type: PhoneNumberType | null;
}
