import {AbstractControl, AsyncValidator, ValidationErrors} from '@angular/forms';
import {RegistrationService} from '../../services/registration.service';
import {environment} from '../../../environments/environment';

export class OrganizationValidator implements AsyncValidator {

    constructor(private readonly registrationService: RegistrationService) {
    }

    validate(control: AbstractControl): Promise<ValidationErrors | null> {

        const value = control.value as string;

        if (!value) {
            return Promise.resolve(null);
        }

        if (value.length < environment.orgNameMinLength || value.length > environment.orgNameMaxLength) {
            return Promise.resolve({'wrong length': value.length});
        }

        return this.registrationService.exist(value)
            .then(exist => this.convertToResult(exist, value));
    }

    convertToResult(exist: boolean, value: string): ValidationErrors | null {
        if (exist) {
            return {'already exists': value};
        } else {
            return null;
        }
    }
}