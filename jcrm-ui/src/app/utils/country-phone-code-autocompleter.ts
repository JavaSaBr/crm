import {CountryRepository} from '../repositories/country/country.repository';
import {AbstractControl} from '@angular/forms';
import {Observable, Subject} from 'rxjs';
import {Country} from '../entity/country';
import {Utils} from './utils';

export class CountryPhoneCodeAutocompleter {

    private readonly filteredCountries = new Subject<Country[]>();

    constructor(
        private countryRepository: CountryRepository,
        private control: AbstractControl
    ) {
        control.valueChanges.subscribe(nameOrPhoneCode => this.filterByNameOrPhoneCode(nameOrPhoneCode));
        countryRepository.findAll()
            .then(value => this.filteredCountries.next(value));
    }

    private filterByNameOrPhoneCode(value: string) {

        if (!value) {
            this.countryRepository.findAll()
                .then(countries => this.filteredCountries.next(countries));
        } else if (value.startsWith('+') || Utils.isNumber(value)) {
            this.countryRepository.findAll()
                .then(countries => {
                    this.filteredCountries.next(countries.filter(country => country.phoneCode.includes(value)));
                });
        } else {
            value = value.toLowerCase();
            this.countryRepository.findAll()
                .then(countries => {
                    this.filteredCountries.next(countries.filter(country => country.nameInLowerCase.includes(value)));
                });
        }
    }

    public getFilteredCountries(): Observable<Country[]> {
        return this.filteredCountries;
    }
}
