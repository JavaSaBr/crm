import {Injectable} from '@angular/core';
import {UniqEntity} from '@app/entity/uniq-entity';
import {SecurityService} from '@app/service/security.service';
import {ErrorResponse} from '@app/error/error-response';
import {TranslateService} from '@ngx-translate/core';
import {RemoteRepository} from '@app/repository/remote.repository';

@Injectable({
    providedIn: 'root'
})
export class AsyncEntityRemoteRepository<T extends UniqEntity, R> extends RemoteRepository<T, R> {

    protected constructor(
        securityService: SecurityService,
        translateService: TranslateService,
    ) {
        super(securityService, translateService);
    }

    public findAll(): Promise<T[]> {
        return this.securityService.getRequest<R[]>(this.buildFetchUrl())
            .then(value => value.body.map(resource => this.convertAsync(resource)))
            .then(promises => Promise.all(promises))
            .catch(resp => {
                ErrorResponse.convertToErrorOrNull(resp, this.translateService);
                return [];
            });
    }

    public findById(id: number): Promise<T | null> {
        return this.securityService.getRequest<R>(this.buildFetchUrlById(id))
            .then(value => this.convertAsync(value.body))
            .catch(resp => {
                ErrorResponse.convertToErrorOrNull(resp, this.translateService);
                return null;
            });
    }

    public findByIds(ids: number[]): Promise<T[]> {
        return this.securityService.postRequest<R[]>(this.buildFetchUrlByIds(), ids)
            .then(value => value.body.map(resource => this.convertAsync(resource)))
            .then(promises => Promise.all(promises))
            .catch(resp => {
                ErrorResponse.convertToErrorOrNull(resp, this.translateService);
                return null;
            });
    }

    protected convertAsync(resource: R): Promise<T> {
        throw new Error('`convertAsync` is not yet implemented');
    }
}
