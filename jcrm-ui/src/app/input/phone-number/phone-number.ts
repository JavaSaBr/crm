import {Country} from '../../entity/country';

export class PhoneNumber {
    constructor(public country: Country, public phoneNumber: string) {
    }
}