import {CountryRepository} from '@app/repository/country/country.repository';
import {AbstractControl} from '@angular/forms';
import {Observable} from 'rxjs';
import {Country} from '@app/entity/country';
import {Utils} from '@app/util/utils';
import {BaseAutoCompleter} from '@app/util/auto-completer/base-auto-completer';

export class CountryPhoneCodeAutoCompleter extends BaseAutoCompleter<Country> {

    public static install(control: AbstractControl, countryRepository: CountryRepository): Observable<Country[]> {
        return new CountryPhoneCodeAutoCompleter(control, countryRepository)._filteredElements;
    }

    constructor(
        control: AbstractControl,
        private readonly countryRepository: CountryRepository
    ) {
        super(control);
        this.resetToDefault();
    }

    protected resetToDefault(): void {
        this.countryRepository.findAll()
            .then(countries => this._filteredElements.next(countries));
    }

    protected searchTimeout(): number {
        return 0;
    }

    protected searchByString(value: string): void {

        if (value.startsWith('+') || Utils.isNumber(value)) {
            this.countryRepository.findAll()
                .then(countries => {
                    this._filteredElements.next(countries.filter(country => country.phoneCode.includes(value)));
                });
        } else {
            this.countryRepository.findAll()
                .then(countries => {
                    this._filteredElements.next(countries.filter(country => country.nameInLowerCase.includes(value)));
                });
        }
    }
}
