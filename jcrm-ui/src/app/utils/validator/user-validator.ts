import {AbstractControl, AsyncValidator, FormControl, ValidationErrors} from '@angular/forms';
import {RegistrationService} from '../../services/registration.service';
import {environment} from '../../../environments/environment';
import {BaseLazyAsyncValidator} from "./base-lazy-async-validator";

export class UserValidator extends BaseLazyAsyncValidator<boolean> {

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