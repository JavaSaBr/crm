import {AbstractControl, AsyncValidator, ValidationErrors} from '@angular/forms';
import {RegistrationService} from '../../services/registration.service';
import {environment} from '../../../environments/environment';
import {BaseLazyAsyncValidator} from "./base-lazy-async-validator";

export class UserValidator extends BaseLazyAsyncValidator<boolean> {

    constructor(private readonly registrationService: RegistrationService) {
        super();
    }

    convertToResult(exist: boolean, value: string): ValidationErrors | null {
        if (exist) {
            return {'already exists': value};
        } else {
            return null;
        }
    }

    validateSync(value): ValidationErrors {
        if (value.length < environment.emailMinLength || value.length > environment.emailMaxLength) {
            return {'wrong length': value.length};
        } else {
            return null;
        }
    }

    validateAsync(value): Promise<boolean> {
        return this.registrationService.userExistByName(value);
    }
}