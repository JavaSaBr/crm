import {AbstractControl, FormControl, ValidationErrors, Validator} from '@angular/forms';
import {Country} from '../../entity/country';
import {TranslateService} from '@ngx-translate/core';

export class CountryValidator implements Validator {

    public static readonly instance = new CountryValidator();

    static getErrorDescription(control: FormControl, translateService: TranslateService): string | null {

        if (control.hasError('required')) {
            return translateService.instant('FORMS.ERROR.COUNTRY.REQUIRED');
        }

        return null;
    }

    validate(control: AbstractControl): ValidationErrors | null {

        const value = control.value;

        if (value) {

            const country = value as Country;

            if (!country.id) {
                return {'noCountry': true};
            }
        }

        return null;
    }
}
