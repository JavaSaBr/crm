import {PhoneNumber} from '@app/entity/phone-number';

export class PhoneNumberResource {

    public static of(phoneNumber: PhoneNumber): PhoneNumberResource {
        return new PhoneNumberResource(
            phoneNumber.countryCode,
            phoneNumber.regionCode,
            phoneNumber.phoneNumber,
            PhoneNumber.getPhoneTypeId(phoneNumber.type)
        );
    }

    public static toPhoneNumber(resource: PhoneNumberResource): PhoneNumber {
        return new PhoneNumber(
            resource.countryCode,
            resource.regionCode,
            resource.phoneNumber,
            PhoneNumber.getPhoneTypeById(resource.type)
        );
    }

    constructor(
        public readonly countryCode: string,
        public readonly regionCode: string,
        public readonly phoneNumber: string,
        public readonly type: number
    ) {}
}
