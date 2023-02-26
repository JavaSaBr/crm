import {AbstractControl, ValidationErrors, Validator} from '@angular/forms';
import {environment} from '@app/env/environment';

export class OtherUserNameValidator implements Validator {

    public static readonly instance = new OtherUserNameValidator();

    validate(control: AbstractControl): ValidationErrors | null {

        const value = control.value as string;

        if (!value) {
            return null;
        } else if (value.length < environment.otherUserNameMinLength || value.length > environment.otherUserNameMaxLength) {
            return {'wrong length': value.length};
        } else {
            return null;
        }
    }
}
