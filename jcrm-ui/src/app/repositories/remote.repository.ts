import {Injectable} from '@angular/core';
import {Repository} from '@app/repository/repository';
import {Entity} from '@app/entity/entity';
import {SecurityService} from '@app/service/security.service';

@Injectable({
    providedIn: 'root'
})
export class RemoteRepository<T extends Entity> implements Repository<T> {

    protected constructor(protected securityService: SecurityService) {
    }

    public findAll(): Promise<T[]> {
        return this.securityService.getRequest<T[]>(this.buildFetchUrl())
            .then(value => value.body);
    }

    public findById(id: number): Promise<T | null> {
        return this.securityService.getRequest<T>(this.buildFetchUrlById(id))
            .then(value => value.body);
    }

    protected buildFetchUrl(): string {
        throw new Error('Not yet implemented');
    }

    protected buildFetchUrlById(id: number): string {
        throw new Error('Not yet implemented');
    }
}
