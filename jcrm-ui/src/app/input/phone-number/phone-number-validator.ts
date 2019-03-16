import {Directive} from '@angular/core';
import {AbstractControl, NG_VALIDATORS, ValidationErrors, Validator} from '@angular/forms';
import {PhoneNumber} from './phone-number';

@Directive({
    selector: 'phoneNumber',
    providers: [
        {provide: NG_VALIDATORS, useExisting: PhoneNumberValidator, multi: true}
    ]
})
export class PhoneNumberValidator implements Validator {

    validate(control: AbstractControl): ValidationErrors | null {

        const value = control.value;

        if (value instanceof PhoneNumber) {

            const phoneNumber: PhoneNumber = value;

            if (phoneNumber.country == null) {
                return {'no country code': {value: phoneNumber}};
            } else if (!this.validatePhoneNumber(phoneNumber.phoneNumber)) {
                return {'phone number is not valid': {value: phoneNumber}};
            }
        }

        return null;
    }

    validatePhoneNumber(phoneNumber: string): boolean {
        return ((phoneNumber != null) && !isNaN(Number(phoneNumber)));
    }
}