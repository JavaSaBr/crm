import {ContactPhoneNumber} from '@app/entity/contact-phone-number';

export class ContactPhoneNumberResource {

    public static valueOf(contactPhoneNumber: ContactPhoneNumber): ContactPhoneNumberResource {

        const phoneNumber = contactPhoneNumber.phoneNumber;

        return new ContactPhoneNumberResource(
            phoneNumber.country.phoneCode,
            phoneNumber.regionCode,
            phoneNumber.phoneNumber,
            contactPhoneNumber.type
        );
    }

    constructor(
        countryCode: string,
        regionCode: string,
        phoneNumber: string,
        type: string
    ) {
    }
}
