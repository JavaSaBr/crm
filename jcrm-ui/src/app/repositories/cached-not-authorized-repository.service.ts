import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Repository} from '@app/repository/repository';
import {UniqEntity} from '@app/entity/uniq-entity';

@Injectable({
    providedIn: 'root'
})
export class CachedNotAuthorizedRepository<T extends UniqEntity> implements Repository<T> {

    protected cache: T[] | null;
    protected executing: Promise<T[]> | null;

    protected constructor(protected httpClient: HttpClient) {
        this.cache = null;
        this.executing = null;
    }

    public findAll(): Promise<T[]> {

        if (this.cache != null) {
            return Promise.resolve(this.cache);
        } else if (this.executing != null) {
            return this.executing;
        }

        this.executing = this.httpClient.get<T[]>(this.buildFetchUrl())
            .toPromise()
            .then(value => {
                this.cache = this.extractValue(value);
                this.executing = null;
                return this.cache;
            })
            .catch(() => {
                this.executing = null;
                return this.cache;
            });

        return this.executing;
    }

    public findById(id: number): Promise<T | null> {
        return this.findAll()
            .then(values => {
                let result = values.find(value => value.id == id);
                return result ? result : null;
            });
    }

    protected buildFetchUrl(): string {
        throw new Error('Not yet implemented');
    }

    protected extractValue(value: T[]): T[] {
        throw new Error('Not yet implemented');
    }
}
