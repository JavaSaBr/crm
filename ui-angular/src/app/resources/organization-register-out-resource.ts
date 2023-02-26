import {PhoneNumberResource} from '@app/resource/phone-number-resource';

export class OrganizationRegisterOutResource {

    constructor(
        private readonly orgName: string,
        private readonly email: string,
        private readonly activationCode: string,
        private readonly password: string,
        private readonly firstName: string,
        private readonly secondName: string,
        private readonly phoneNumber: PhoneNumberResource,
        private readonly subscribe: boolean,
        private readonly countryId: number
    ) {
    }
}
