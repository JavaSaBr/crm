import {ContactPhoneNumber} from '@app/entity/contact-phone-number';

export class ContactPhoneNumberResource {

    public static valueOf(contactPhoneNumber: ContactPhoneNumber): ContactPhoneNumberResource {

        const phoneNumber = contactPhoneNumber.phoneNumber;
        const country = phoneNumber.country;

        return new ContactPhoneNumberResource(
            country ? country.phoneCode : null,
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
