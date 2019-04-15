import {FormControl, ValidationErrors} from '@angular/forms';
import {RegistrationService} from '../../services/registration.service';
import {environment} from '../../../environments/environment';
import {BaseLazyAsyncValidator} from "./base-lazy-async-validator";
import {TranslateService} from '@ngx-translate/core';

export class OrganizationValidator extends BaseLazyAsyncValidator<boolean> {

    static getNameErrorDescription(control: FormControl, translateService: TranslateService): string | null {

        if (control.hasError('required')) {
            return translateService.instant('FORMS.ERROR.ORG.NAME.REQUIRED');
        } else if (OrganizationValidator.isTooShort(control)) {
            return translateService.instant('FORMS.ERROR.ORG.NAME.TOO_SHORT');
        } else if (OrganizationValidator.isTooLong(control)) {
            return translateService.instant('FORMS.ERROR.ORG.NAME.TOO_LONG');
        } else if (OrganizationValidator.isAlreadyExist(control)) {
            return translateService.instant('FORMS.ERROR.ORG.NAME.ALREADY_EXIST');
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

    validateSync(value): ValidationErrors {

        if (value.length < environment.orgNameMinLength) {
            return {'tooShort': true};
        } else if (value.length > environment.orgNameMaxLength) {
            return {'tooLong': true};
        }

        return null;
    }

    validateAsync(value): Promise<boolean> {
        return this.registrationService.orgExistByName(value);
    }

    convertToResult(exist: boolean, value: string): ValidationErrors | null {
        if (exist) {
            return {'alreadyExists': true};
        } else {
            return null;
        }
    }
}
