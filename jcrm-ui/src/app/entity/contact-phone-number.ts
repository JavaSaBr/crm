import {Entity} from '@app/entity/entity';
import {PhoneNumber} from '@app/entity/phone-number';

export enum PhoneNumberType {
    WORK = 'WORK',
    MOBILE = 'MOBILE'
}

export class ContactPhoneNumber extends Entity {
    constructor(public phoneNumber: PhoneNumber | null, public type: PhoneNumberType | null) {
        super();
    }
}
