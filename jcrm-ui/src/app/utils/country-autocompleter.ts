import {CountryRepository} from '../repositories/country/country.repository';
import {AbstractControl} from '@angular/forms';
import {Observable, Subject} from 'rxjs';
import {Country} from '@app/entity/country';

export class CountryAutocompleter {

    private readonly _filteredCountries = new Subject<Country[]>();

    constructor(
        private countryRepository: CountryRepository,
        private control: AbstractControl
    ) {
        control.valueChanges.subscribe(countryName => this.filterByName(countryName.toString().toLowerCase()));
        countryRepository.findAll()
            .then(value => this._filteredCountries.next(value));
    }

    private filterByName(countryName: string) {
        this.countryRepository.findAll()
            .then(countries => {
                this._filteredCountries.next(countries.filter(country => country.nameInLowerCase.includes(countryName)));
            });
    }

    get filteredCountries(): Observable<Country[]> {
        return this._filteredCountries;
    }
}
