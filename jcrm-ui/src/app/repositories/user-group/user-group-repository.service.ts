import {Injectable} from '@angular/core';
import {environment} from '@app/env/environment';
import {RemoteRepository} from '@app/repository/remote.repository';
import {SecurityService} from '@app/service/security.service';
import {ErrorService} from '@app/service/error.service';
import {UserGroup} from '@app/entity/user-group';
import {UserGroupResource} from '@app/resource/user-group-resource';

@Injectable({
    providedIn: 'root'
})
export class UserGroupRepository extends RemoteRepository<UserGroup, UserGroupResource> {

    constructor(
        securityService: SecurityService,
        errorService: ErrorService
    ) {
        super(securityService, errorService);
    }

    public create(userGroup: UserGroup): Promise<UserGroup> {

        const body = this.convertToResource(userGroup);
        const url = `${environment.registrationUrl}/user-group`;

        return this.securityService.postRequest<UserGroupResource>(url, body)
            .then(response => this.convertFromResource(response.body))
            .catch(reason => this.errorService.convertError(reason));
    }

    protected buildFetchUrlById(id: number): string {
        return `${environment.registrationUrl}/user-group/${id}`;
    }

    protected buildFetchUrlByIds(): string {
        return `${environment.registrationUrl}/user-groups/ids`;
    }

    protected buildEntityPageFetchUrl(pageSize: number, offset: number): string {
        return `${environment.registrationUrl}/user-groups/page?pageSize=${pageSize}&offset=${offset}`;
    }

    protected convertFromResource(resource: UserGroupResource): UserGroup {
        return new UserGroup(
            resource.id,
            resource.name,
            resource.created ? new Date(resource.created) : null,
            resource.modified ? new Date(resource.modified) : null
        );
    }

    private convertToResource(userGroup: UserGroup): UserGroupResource {
        return new UserGroupResource(
            userGroup.id,
            userGroup.name,
            userGroup.created ? userGroup.created.getTime() : null,
            userGroup.modified ? userGroup.modified.getTime() : null
        );
    }
}
