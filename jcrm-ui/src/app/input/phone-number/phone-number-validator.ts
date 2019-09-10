import {AbstractControl, FormControl, ValidationErrors, Validator, ValidatorFn} from '@angular/forms';
import {PhoneNumber} from '@app/entity/phone-number';
import {Utils} from '@app/util/utils';
import {environment} from '@app/env/environment';
import {TranslateService} from '@ngx-translate/core';

export class PhoneNumberValidator implements Validator {

    public static readonly INSTANCE = new PhoneNumberValidator();
    public static readonly FUN: ValidatorFn = (contorl: AbstractControl) => {
        return PhoneNumberValidator.INSTANCE.validate(contorl);
    };

    static getErrorDescription(control: FormControl, translateService: TranslateService): string | null {

        if (control.hasError('required')) {
            return translateService.instant('FORMS.ERROR.PHONE_NUMBER.REQUIRED');
        } else if (PhoneNumberValidator.isNoCountry(control)) {
            return translateService.instant('FORMS.ERROR.PHONE_NUMBER.NO_COUNTRY');
        } else if (PhoneNumberValidator.isInvalidPhoneNumber(control)) {
            return translateService.instant('FORMS.ERROR.PHONE_NUMBER.INVALID');
        } else if (PhoneNumberValidator.isTooShort(control)) {
            return translateService.instant('FORMS.ERROR.PHONE_NUMBER.TOO_SHORT');
        } else if (PhoneNumberValidator.isTooLong(control)) {
            return translateService.instant('FORMS.ERROR.PHONE_NUMBER.TOO_LONG');
        }

        return null;
    }

    static isTooShort(control: FormControl) {
        return control.hasError('tooShort');
    }

    static isTooLong(control: FormControl) {
        return control.hasError('tooLong');
    }

    static isInvalidPhoneNumber(control: FormControl) {
        return control.hasError('invalidPhoneNumber');
    }

    static isNoCountry(control: FormControl) {
        return control.hasError('noCountry');
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
