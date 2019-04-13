import {AbstractControl, FormControl, ValidationErrors, Validator} from '@angular/forms';
import {Country} from '../../entity/country';

export class CountryValidator implements Validator {

    public static readonly instance = new CountryValidator();

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