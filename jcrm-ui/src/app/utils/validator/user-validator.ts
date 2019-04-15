import {AbstractControl, AsyncValidator, FormControl, ValidationErrors} from '@angular/forms';
import {RegistrationService} from '../../services/registration.service';
import {environment} from '../../../environments/environment';
import {BaseLazyAsyncValidator} from "./base-lazy-async-validator";
import {TranslateService} from '@ngx-translate/core';

export class UserValidator extends BaseLazyAsyncValidator<boolean> {

    public static readonly EMAIL_PATTERN = '^[_A-Za-z0-9-]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$';

    static getEmailErrorDescription(control: FormControl, translateService: TranslateService): string | null {

        if (control.hasError('required')) {
            return translateService.instant('FORMS.ERROR.USER.EMAIL.REQUIRED');
        } else if (control.hasError('pattern')) {
            return translateService.instant('FORMS.ERROR.USER.EMAIL.INVALID');
        } else if (UserValidator.isTooShort(control)) {
            return translateService.instant('FORMS.ERROR.USER.EMAIL.TOO_SHORT');
        } else if (UserValidator.isTooLong(control)) {
            return translateService.instant('FORMS.ERROR.USER.EMAIL.TOO_LONG');
        } else if (UserValidator.isAlreadyExist(control)) {
            return translateService.instant('FORMS.ERROR.USER.EMAIL.ALREADY_EXIST');
        }

        return null;
    }

    static isTooShort(control: FormControl) {
        return control.hasError('tooShort')
    }

    static isTooLong(control: FormControl) {
        return control.hasError('tooLong')
    }

    static isAlreadyExist(control: FormControl) {
        return control.hasError('alreadyExists')
    }

    constructor(private readonly registrationService: RegistrationService) {
        super();
    }

    convertToResult(exist: boolean, value: string): ValidationErrors | null {
        if (exist) {
            return {'alreadyExists': value};
        } else {
            return null;
        }
    }

    validateSync(value): ValidationErrors {
        if (value.length < environment.emailMinLength) {
            return {'tooShort': true};
        } if (value.length > environment.emailMaxLength) {
            return {'tooLong': true};
        } else {
            return null;
        }
    }

    validateAsync(value): Promise<boolean> {
        return this.registrationService.userExistByName(value);
    }
}
