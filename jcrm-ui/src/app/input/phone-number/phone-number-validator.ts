import {Directive} from '@angular/core';
import {AbstractControl, FormControl, NG_VALIDATORS, ValidationErrors, Validator} from '@angular/forms';
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

    static isTooShort(control: FormControl) {
        return control.hasError('tooShort')
    }

    static isTooLong(control: FormControl) {
        return control.hasError('tooLong')
    }

    static isInvalidPhoneNumber(control: FormControl) {
        return control.hasError('invalidPhoneNumber')
    }

    static isNoCountry(control: FormControl) {
        return control.hasError('noCountry')
    }

    validate(control: AbstractControl): ValidationErrors | null {

        const value = control.value;

        if (value instanceof PhoneNumber) {

            const phoneNumber: PhoneNumber = value;

            if (phoneNumber.country == null) {
                return {'noCountry': true};
            } else if (!Utils.isNumber(phoneNumber.phoneNumber)) {
                return {'invalidPhoneNumber': true};
            }

            let phoneCode = phoneNumber.country.phoneCode;
            let phoneNumberLength = phoneNumber.phoneNumber.length + phoneCode.length;

            if (phoneNumberLength < environment.phoneNumberMinLength) {
                return {'tooShort': true};
            } else if (phoneNumberLength > environment.phoneNumberMaxLength) {
                return {'tooLong': phoneNumberLength};
            }

        } else if (value) {
            return {'invalidPhoneNumber': true};
        }

        return null;
    }
}