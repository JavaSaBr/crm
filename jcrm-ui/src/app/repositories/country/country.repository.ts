import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Country} from '@app/entity/country';
import {environment} from '@app/env/environment';
import {CachedNotAuthorizedRepository} from '@app/repository/cached-not-authorized-repository.service';

@Injectable({
    providedIn: 'root'
})
export class CountryRepository extends CachedNotAuthorizedRepository<Country> {

    constructor(protected httpClient: HttpClient) {
        super(httpClient);
    }

    protected buildFetchUrl(): string {
        return environment.dictionaryUrl + '/countries';
    }

    protected extractValue(value: Country[]): Country[] {
        value.forEach(country => country.nameInLowerCase = country.name.toLowerCase());
        return value;
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
