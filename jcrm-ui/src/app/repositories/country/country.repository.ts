import {Injectable} from '@angular/core';
import {Country} from '@app/entity/country';
import {environment} from '@app/env/environment';
import {CachedNotAuthorizedRepository} from '@app/repository/cached-not-authorized-repository.service';

@Injectable({
    providedIn: 'root'
})
export class CountryRepository extends CachedNotAuthorizedRepository<Country> {

    protected buildFetchUrl(): string {
        return environment.dictionaryUrl + '/countries';
    }

    protected extractValue(countries: Country[]): Country[] {
        return countries.map(country => Country.copy(country));
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
