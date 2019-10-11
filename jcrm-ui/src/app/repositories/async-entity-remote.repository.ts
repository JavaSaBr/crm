import {Injectable} from '@angular/core';
import {UniqEntity} from '@app/entity/uniq-entity';
import {SecurityService} from '@app/service/security.service';
import {RemoteRepository} from '@app/repository/remote.repository';
import {DataPageResource} from '@app/resource/data-page-resource';
import {EntityPage} from '@app/entity/entity-page';
import {ErrorService} from '@app/service/error.service';

@Injectable({
    providedIn: 'root'
})
export class AsyncEntityRemoteRepository<T extends UniqEntity, R> extends RemoteRepository<T, R> {

    protected constructor(
        securityService: SecurityService,
        errorService: ErrorService,
    ) {
        super(securityService, errorService);
    }

    public findAll(): Promise<T[]> {
        return this.securityService.getRequest<R[]>(this.buildFetchUrl())
            .then(value => value.body.map(resource => this.convertAsync(resource)))
            .then(promises => Promise.all(promises))
            .catch(reason => this.errorService.convertError(reason));
    }

    public findById(id: number): Promise<T | null> {
        return this.securityService.getRequest<R>(this.buildFetchUrlById(id))
            .then(value => this.convertAsync(value.body))
            .catch(reason => this.errorService.convertError(reason));
    }

    public findByIds(ids: number[]): Promise<T[]> {
        if (ids.length < 1) {
            return Promise.resolve([]);
        } else {
            return this.securityService.postRequest<R[]>(this.buildFetchUrlByIds(), ids)
                .then(value => value.body.map(resource => this.convertAsync(resource)))
                .then(promises => Promise.all(promises))
                .catch(reason => this.errorService.convertError(reason));
        }
    }

    public findEntityPage(pageSize: number, offset: number): Promise<EntityPage<T>> {
        return this.securityService.getRequest<DataPageResource<R>>(this.buildEntityPageFetchUrl(pageSize, offset))
            .then(value => {
                const resource = value.body;
                return Promise.all(resource.resources.map(resource => this.convertAsync(resource)))
                    .then(entities => {
                        return new EntityPage(entities, resource.totalSize);
                    })
            })
            .catch(reason => this.errorService.convertError(reason));
    }

    protected convertAsync(resource: R): Promise<T> {
        throw new Error('`convertAsync` is not yet implemented');
    }
}
