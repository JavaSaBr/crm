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
        return value.countries;
    }
}
