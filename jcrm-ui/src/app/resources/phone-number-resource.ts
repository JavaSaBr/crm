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

    public static toPhoneNumbers(resources: PhoneNumberResource[]): PhoneNumber[] | null {
        if (resources == null || resources.length == 0) {
            return null;
        } else {
            return resources.map(value => value.toPhoneNumber());
        }
    }

    constructor(
        public readonly countryCode: string,
        public readonly regionCode: string,
        public readonly phoneNumber: string,
        public readonly type: number
    ) {
    }

    public toPhoneNumber(): PhoneNumber {
        return new PhoneNumber(
            this.countryCode,
            this.regionCode,
            this.phoneNumber,
            PhoneNumber.getPhoneTypeById(this.type)
        );
    }
}
