import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Repository} from './repository';

@Injectable({
    providedIn: 'root'
})
export class CachedRepository<T> implements Repository<T> {

    protected cache: T[] = null;

    protected constructor(protected httpClient: HttpClient) {
    }

    public findAll(): Promise<T[]> {

        if (this.cache != null) {
            return Promise.resolve(this.cache);
        }

        return new Promise<T[]>((resolve, reject) => {

            this.httpClient.get<T[]>(this.buildFetchUrl())
                .subscribe(value => {
                        this.cache = this.extractValue(value);
                        resolve(this.cache);
                    },
                    error => {
                        reject(error);
                    });
        });
    }

    protected buildFetchUrl(): string {
        throw new Error('Not yet implemented');
    }

    protected extractValue(value): T[] {
        throw new Error('Not yet implemented');
    }
}
