import {Injectable} from '@angular/core';
import {Repository} from '@app/repository/repository';
import {UniqEntity} from '@app/entity/uniq-entity';
import {SecurityService} from '@app/service/security.service';
import {ErrorResponse} from '@app/error/error-response';
import {TranslateService} from '@ngx-translate/core';
import {DataPageResource} from '@app/resource/data-page-resource';
import {EntityPage} from '@app/entity/entity-page';

@Injectable({
    providedIn: 'root'
})
export class RemoteRepository<T extends UniqEntity, R> implements Repository<T> {

    protected constructor(
        protected readonly securityService: SecurityService,
        protected readonly translateService: TranslateService,
    ) {
    }

    public findAll(): Promise<T[]> {
        return this.securityService.getRequest<R[]>(this.buildFetchUrl())
            .then(value => value.body.map(resource => this.convert(resource)))
            .catch(resp => {
                ErrorResponse.convertToErrorOrNull(resp, this.translateService);
                return [];
            });
    }

    public findById(id: number): Promise<T | null> {
        return this.securityService.getRequest<R>(this.buildFetchUrlById(id))
            .then(value => this.convert(value.body))
            .catch(resp => {
                ErrorResponse.convertToErrorOrNull(resp, this.translateService);
                return null;
            });
    }

    public findByIds(ids: number[]): Promise<T[]> {
        return this.securityService.postRequest<R[]>(this.buildFetchUrlByIds(), ids)
            .then(value => value.body.map(entity => this.convert(entity)))
            .catch(resp => {
                ErrorResponse.convertToErrorOrNull(resp, this.translateService);
                return null;
            });
    }

    public findEntityPage(pageSize: number, offset: number): Promise<EntityPage<T>> {
        return this.securityService.getRequest<DataPageResource<R>>(this.buildEntityPageFetchUrl(pageSize, offset))
            .then(value => {
                const resource = value.body;
                const entities = resource.resources.map(resource => this.convert(resource));
                return new EntityPage(entities, resource.totalSize);
            })
            .catch(resp => {
                ErrorResponse.convertToErrorOrNull(resp, this.translateService);
                return null;
            });
    }

    protected convert(resource: R): T {
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
