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

    private filterByNameOrPhoneCode(value: any) {

        if (!value) {
            this.countryRepository.findAll()
                .then(countries => this.filteredCountries.next(countries));
            return;
        }

        const countryValue = value as Country;

        if (countryValue.id) {
            this.countryRepository.findAll()
                .then(countries => {
                    this.filteredCountries.next(countries.filter(country => country.id == countryValue.id));
                });
        }

        const stringValue = value.toString()
            .toLowerCase();

        if (stringValue.startsWith('+') || Utils.isNumber(stringValue)) {
            this.countryRepository.findAll()
                .then(countries => {
                    this.filteredCountries.next(countries.filter(country => country.phoneCode.includes(stringValue)));
                });
        } else {
            this.countryRepository.findAll()
                .then(countries => {
                    this.filteredCountries.next(countries.filter(country => country.nameInLowerCase.includes(stringValue)));
                });
        }
    }

    public getFilteredCountries(): Observable<Country[]> {
        return this.filteredCountries;
    }
}
