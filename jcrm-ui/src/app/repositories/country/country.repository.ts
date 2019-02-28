import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Repository} from '../repository';
import {Country} from '../../entity/country';
import {environment} from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class CountryRepository implements Repository<Country> {

    private cache: Country[] = null;

    constructor(private httpClient: HttpClient, private env: environment) {
    }

    findAll(): Promise<Country[]> {

        if (this.cache != null) {
            return Promise.resolve(this.cache);
        }

        return new Promise<Country[]>((resolve, reject) => {

            this.httpClient.get<Country[]>(this.env.dictionaryUrl + '/countries')
                .subscribe(value => {
                        this.cache = value;
                        resolve(value);
                    },
                    error => {
                        reject(error);
                    });
        });
    }
}
