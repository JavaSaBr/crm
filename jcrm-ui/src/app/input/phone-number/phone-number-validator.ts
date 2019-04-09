import {Directive} from '@angular/core';
import {AbstractControl, NG_VALIDATORS, ValidationErrors, Validator} from '@angular/forms';
import {PhoneNumber} from './phone-number';
import {Utils} from '../../utils/utils';
import {environment} from "../../../environments/environment";

@Directive({
    selector: 'phoneNumber',
    providers: [
        {provide: NG_VALIDATORS, useExisting: PhoneNumberValidator, multi: true}
    ]
})
export class PhoneNumberValidator implements Validator {

    public static readonly instance = new PhoneNumberValidator();

    validate(control: AbstractControl): ValidationErrors | null {

        const value = control.value;

        if (value instanceof PhoneNumber) {

            const phoneNumber: PhoneNumber = value;

            if (phoneNumber.country == null) {
                return {'no country code': {value: phoneNumber}};
            } else if (!Utils.isNumber(phoneNumber.phoneNumber)) {
                return {'phone number is not valid': {value: phoneNumber}};
            }

            let phoneCode = phoneNumber.country.phoneCode;
            let phoneNumberLength = phoneNumber.phoneNumber.length + phoneCode.length;

            if (phoneNumberLength < environment.phoneNumberMinLength ||
                phoneNumberLength > environment.phoneNumberMaxLength
            ) {
                return {'wrong length': phoneNumberLength};
            }
        }

        return null;
    }
}