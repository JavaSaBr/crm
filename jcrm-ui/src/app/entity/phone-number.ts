import {Entity} from '@app/entity/entity';

export enum PhoneNumberType {
    UNKNOWN = "UNKNOWN",
    WORK = 'WORK',
    MOBILE = 'MOBILE'
}

export class PhoneNumber extends Entity {

    private static readonly phoneTypeToId = new Map<PhoneNumberType, number>([
        [PhoneNumberType.UNKNOWN, 0],
        [PhoneNumberType.MOBILE, 1],
        [PhoneNumberType.WORK, 2],
    ])

    private static readonly idToPhoneType = new Map<number, PhoneNumberType>([
        [0, PhoneNumberType.UNKNOWN],
        [1, PhoneNumberType.MOBILE],
        [2, PhoneNumberType.WORK],
    ])

    public static getPhoneTypeId(phoneType: PhoneNumberType): number {
        if (phoneType == null) {
            return 0;
        } else {
            return this.phoneTypeToId.get(phoneType);
        }
    }

    public static getPhoneTypeById(phoneTypeId: number): PhoneNumberType | null {
        return this.idToPhoneType.get(phoneTypeId);
    }

    constructor(
        public countryCode: string | null,
        public regionCode: string | null,
        public phoneNumber: string | null,
        public type: PhoneNumberType | null,
    ) {
        super();
    }
}
