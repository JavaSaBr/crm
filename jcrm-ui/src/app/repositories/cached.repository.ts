import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Repository} from './repository';
import {Entity} from '../entity/entity';

@Injectable({
    providedIn: 'root'
})
export class CachedRepository<T extends Entity> implements Repository<T> {

    protected cache: T[] = null;
    protected executing: Promise<T[]> = null;

    protected constructor(protected httpClient: HttpClient) {
    }

    public findAll(): Promise<T[]> {

        if (this.cache != null) {
            return Promise.resolve(this.cache);
        } else if (this.executing != null) {
            return this.executing;
        }

        return new Promise<T[]>((resolve, reject) => {

            this.httpClient.get<T[]>(this.buildFetchUrl())
                .subscribe(value => {
                        this.cache = this.extractValue(value);
                        this.executing = null;
                        resolve(this.cache);
                    },
                    error => {
                        this.executing = null;
                        reject(error);
                    });
        });
    }

    public findById(id: number): Promise<T | null> {
        return this.findAll()
            .then(values => values.find(value => value.id == id));
    }

    protected buildFetchUrl(): string {
        throw new Error('Not yet implemented');
    }

    protected extractValue(value): T[] {
        throw new Error('Not yet implemented');
    }
}
