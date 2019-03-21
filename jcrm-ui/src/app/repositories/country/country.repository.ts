import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Country} from '../../entity/country';
import {environment} from '../../../environments/environment';
import {CachedRepository} from '../cached.repository';

@Injectable({
    providedIn: 'root'
})
export class CountryRepository extends CachedRepository<Country> {

    constructor(protected httpClient: HttpClient) {
        super(httpClient);
    }

    protected buildFetchUrl(): string {
        return environment.dictionaryUrl + '/countries';
    }

    protected extractValue(value): Country[] {

        const countries = value.countries as Country[];
        countries.forEach(country => country.nameInLowerCase = country.name.toLowerCase());

        return countries;
    }

    public findByPhoneCode(phoneCode: string): Promise<Country | null> {
        return this.findAll()
            .then(countries => countries.find(country => country.phoneCode == phoneCode));
    }

    public findByLowerCaseName(name: string): Promise<Country | null> {
        return this.findAll()
            .then(countries => countries.find(country => country.nameInLowerCase == name));
    }
}
