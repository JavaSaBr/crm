import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Repository} from '@app/repository/repository';
import {UniqEntity} from '@app/entity/uniq-entity';
import {ErrorService} from '@app/service/error.service';

@Injectable({
    providedIn: 'root'
})
export class CachedNotAuthorizedRepository<T extends UniqEntity> implements Repository<T> {

    protected cachedMap: Map<number, T> | null;
    protected cachedArray: T[] | null;
    protected executing: Promise<T[]> | null;

    protected constructor(
        protected readonly httpClient: HttpClient,
        protected readonly errorService: ErrorService
    ) {
        this.cachedMap = null;
        this.executing = null;
    }

    public findAll(): Promise<T[]> {

        if (this.cachedMap != null) {
            return Promise.resolve(Array.from(this.cachedMap.values()));
        } else if (this.executing != null) {
            return this.executing;
        }

        this.executing = this.httpClient.get<T[]>(this.buildFetchUrl())
            .toPromise()
            .then(value => {
                this.cachedArray = this.extractValue(value);
                this.cachedMap = new Map<number, T>();
                this.cachedArray.forEach(entity => this.cachedMap.set(entity.id, entity));
                this.executing = null;
                return this.cachedArray;
            })
            .catch(reason => {
                this.executing = null;
                return this.errorService.convertError(reason);
            });

        return this.executing;
    }

    public findById(id: number): Promise<T | null> {
        return this.findAll()
            .then(() => this.cachedMap.get(id));
    }

    protected buildFetchUrl(): string {
        throw new Error('Not yet implemented');
    }

    protected extractValue(value: T[]): T[] {
        throw new Error('Not yet implemented');
    }
}
