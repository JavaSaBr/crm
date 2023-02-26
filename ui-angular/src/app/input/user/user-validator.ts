import {AbstractControl, FormControl, ValidationErrors, Validator} from '@angular/forms';
import {TranslateService} from '@ngx-translate/core';
import {User} from '@app/entity/user';

export class UserValidator implements Validator {

    public static readonly INSTANCE = new UserValidator();

    static getErrorDescription(
        control: FormControl,
        translateService: TranslateService,
        messageKey: string
    ): string | null {

        if (control.hasError('required')) {
            return translateService.instant(messageKey);
        }

        return null;
    }

    validate(control: AbstractControl): ValidationErrors | null {

        const value = control.value;

        if (!value) {
            return null;
        } else if (!(value instanceof User)) {
            return {'noUser': true};
        }

        return null;
    }
}
