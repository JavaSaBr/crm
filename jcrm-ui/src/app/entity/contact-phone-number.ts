import {Entity} from '@app/entity/entity';

export enum PhoneNumberType {
    WORK = 'work',
    MOBILE = 'mobile'
}

export class ContactPhoneNumber extends Entity {
    constructor(public phoneNumber: string | null, public type: PhoneNumberType | null) {
        super();
    }
}
