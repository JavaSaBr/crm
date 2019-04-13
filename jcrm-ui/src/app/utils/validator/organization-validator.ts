import {FormControl, ValidationErrors} from '@angular/forms';
import {RegistrationService} from '../../services/registration.service';
import {environment} from '../../../environments/environment';
import {BaseLazyAsyncValidator} from "./base-lazy-async-validator";

export class OrganizationValidator extends BaseLazyAsyncValidator<boolean> {

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