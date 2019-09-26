import {AbstractControl, FormControl, ValidationErrors, Validator, ValidatorFn} from '@angular/forms';
import {environment} from '@app/env/environment';
import {TranslateService} from '@ngx-translate/core';

export class ContactEmailValidator implements Validator {

    public static readonly INSTANCE: ContactEmailValidator = new ContactEmailValidator();
    public static readonly FUN: ValidatorFn = control => ContactEmailValidator.INSTANCE.validate(control);

    public static readonly EMAIL_PATTERN =
        '^[_A-Za-z0-9-]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$';
    public static readonly EMAIL_REGEX = new RegExp(ContactEmailValidator.EMAIL_PATTERN);

    static getEmailErrorDescription(control: FormControl, translateService: TranslateService): string | null {

        if (control.hasError('required')) {
            return translateService.instant('FORMS.ERROR.EMAIL.REQUIRED');
        } else if (control.hasError('pattern')) {
            return translateService.instant('FORMS.ERROR.EMAIL.INVALID');
        } else if (control.hasError('tooLong')) {
            return translateService.instant('FORMS.ERROR.EMAIL.TOO_LONG');
        }

        return null;
    }

    validate(control: AbstractControl): ValidationErrors | null {

        const value = control.value as string;

        if (!value) {
            return null;
        } else if (value.length > environment.contactEmailMaxLength) {
            return {'tooLong': {'maxLength': environment.contactEmailMaxLength, 'actualLength': value.length}};
        } else if (!ContactEmailValidator.EMAIL_REGEX.test(value)) {
            return {'pattern': {'requiredPattern': ContactEmailValidator.EMAIL_PATTERN, 'actualValue': value}};
        } else {
            return null;
        }
    }
}
