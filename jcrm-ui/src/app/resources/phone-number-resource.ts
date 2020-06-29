import {PhoneNumber} from '@app/entity/phone-number';

export class PhoneNumberResource {

    public static of(phoneNumber: PhoneNumber): PhoneNumberResource {
        return new PhoneNumberResource(
            phoneNumber.country.phoneCode,
            phoneNumber.regionCode,
            phoneNumber.phoneNumber
        );
    }

    constructor(
        private readonly countryCode: string | null,
        private readonly regionCode: string | null,
        private readonly phoneNumber: string | null
    ) {
    }
}
