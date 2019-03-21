import {Directive} from '@angular/core';
import {AbstractControl, NG_VALIDATORS, ValidationErrors, Validator} from '@angular/forms';
import {PhoneNumber} from './phone-number';
import {Utils} from '../../utils/utils';

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
            } else if (!Utils.isNumber(phoneNumber.phoneNumber)) {
                return {'phone number is not valid': {value: phoneNumber}};
            }
        }

        return null;
    }
}