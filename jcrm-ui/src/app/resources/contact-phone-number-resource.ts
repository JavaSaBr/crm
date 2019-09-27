import {ContactPhoneNumber} from '@app/entity/contact-phone-number';

export class ContactPhoneNumberResource {

    public static valueOf(contactPhoneNumber: ContactPhoneNumber): ContactPhoneNumberResource {

        const phoneNumber = contactPhoneNumber.phoneNumber;
        const country = phoneNumber.country;
        const countryCode = country ? country.phoneCode : null;

        return new ContactPhoneNumberResource(
            countryCode,
            phoneNumber.regionCode,
            phoneNumber.phoneNumber,
            contactPhoneNumber.type
        );
    }

    constructor(
        private countryCode: string,
        private regionCode: string,
        private phoneNumber: string,
        private type: string
    ) {
    }
}
