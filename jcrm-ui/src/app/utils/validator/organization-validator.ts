import {ValidationErrors} from '@angular/forms';
import {RegistrationService} from '../../services/registration.service';
import {environment} from '../../../environments/environment';
import {BaseLazyAsyncValidator} from "./base-lazy-async-validator";

export class OrganizationValidator extends BaseLazyAsyncValidator<boolean> {

    constructor(private readonly registrationService: RegistrationService) {
        super();
    }

    validateSync(value): ValidationErrors {

        if (value.length < environment.orgNameMinLength || value.length > environment.orgNameMaxLength) {
            return {'wrong length': value.length};
        }

        return null;
    }

    validateAsync(value): Promise<boolean> {
        return this.registrationService.orgExistByName(value);
    }

    convertToResult(exist: boolean, value: string): ValidationErrors | null {
        if (exist) {
            return {'already exists': value};
        } else {
            return null;
        }
    }
}