import {Injectable} from '@angular/core';
import {Repository} from '@app/repository/repository';
import {UniqEntity} from '@app/entity/uniq-entity';
import {SecurityService} from '@app/service/security.service';
import {DataPageResource} from '@app/resource/data-page-resource';
import {EntityPage} from '@app/entity/entity-page';
import {ErrorService} from '@app/service/error.service';

@Injectable({
    providedIn: 'root'
})
export class RemoteRepository<T extends UniqEntity, R> implements Repository<T> {

    protected constructor(
        protected readonly securityService: SecurityService,
        protected readonly errorService: ErrorService
    ) {
    }

    public findAll(): Promise<T[]> {
        return this.securityService.getRequest<R[]>(this.buildFetchUrl())
            .then(value => value.body.map(resource => this.convertFromResource(resource)))
            .catch(reason => this.errorService.convertError(reason));
    }

    public findById(id: number): Promise<T | null> {
        return this.securityService.getRequest<R>(this.buildFetchUrlById(id))
            .then(value => this.convertFromResource(value.body))
            .catch(reason => this.errorService.convertError(reason));
    }

    public findByIds(ids: number[]): Promise<T[]> {
        if (ids.length < 1) {
            return Promise.resolve([]);
        } else {
            return this.securityService.postRequest<R[]>(this.buildFetchUrlByIds(), ids)
                .then(value => value.body.map(entity => this.convertFromResource(entity)))
                .catch(reason => this.errorService.convertError(reason));
        }
    }

    public findEntityPage(pageSize: number, offset: number): Promise<EntityPage<T>> {
        return this.securityService.getRequest<DataPageResource<R>>(this.buildEntityPageFetchUrl(pageSize, offset))
            .then(value => {

                const resource = value.body;
                const entities = resource.resources
                    .map(resource => this.safeConvertFromResource(resource))
                    .filter(object => object != null);

                return new EntityPage(entities, resource.totalSize);
            })
            .catch(reason => this.errorService.convertError(reason));
    }

    protected safeConvertFromResource(resource: R): T | null {
        try {
            return this.convertFromResource(resource);
        } catch (e: unknown) {
            console.error(`Cannot convert resource: ${resource} by reason: ${e}`);
            return null;
        }
    }

    protected convertFromResource(resource: R): T {
        throw new Error('`convert` is not yet implemented');
    }

    protected buildFetchUrl(): string {
        throw new Error('`buildFetchUrl` is not yet implemented');
    }

    protected buildFetchUrlById(id: number): string {
        throw new Error('`buildFetchUrlById` is not yet implemented');
    }

    protected buildFetchUrlByIds(): string {
        throw new Error('`buildFetchUrlByIds` is not yet implemented');
    }

    protected buildEntityPageFetchUrl(pageSize: number, offset: number): string {
        throw new Error('`buildDataPageFetchUrl` is not yet implemented');
    }
}
