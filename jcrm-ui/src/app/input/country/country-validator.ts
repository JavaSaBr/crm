import {Directive} from '@angular/core';
import {AbstractControl, NG_VALIDATORS, ValidationErrors, Validator} from '@angular/forms';
import {Country} from '../../entity/country';

@Directive({
    selector: 'country',
    providers: [
        {provide: NG_VALIDATORS, useExisting: CountryValidator, multi: true}
    ]
})
export class CountryValidator implements Validator {

    public static readonly instance = new CountryValidator();

    validate(control: AbstractControl): ValidationErrors | null {

        const value = control.value;

        if (value) {

            const country = value as Country;

            if (!country.id) {
                return {'no country': {value: country}};
            }
        }

        return null;
    }
}