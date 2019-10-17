import {Injectable} from '@angular/core';
import {environment} from '@app/env/environment';
import {User} from '@app/entity/user';
import {RemoteRepository} from '@app/repository/remote.repository';
import {MinimalUser} from '@app/entity/minimal-user';
import {MinimalUserResource} from '@app/resource/minimal-user-resource';
import {UserResource} from '@app/resource/user-resource';

@Injectable({
    providedIn: 'root'
})
export class UserRepository extends RemoteRepository<User, UserResource> {

    public findMinimalById(id: number): Promise<MinimalUser | null> {
        return this.securityService.getRequest<MinimalUserResource>(`${environment.registrationUrl}/user/minimal/${id}`)
            .then(response => MinimalUser.from(response.body))
            .catch(reason => this.errorService.convertError(reason));
    }

    public findMinimalByIds(ids: number[]): Promise<MinimalUser[]> {
        if (ids.length < 1) {
            return Promise.resolve([]);
        } else {
            return this.securityService.postRequest<MinimalUserResource[]>(`${environment.registrationUrl}/users/minimal/ids`, ids)
                .then(response => response.body.map(entity => MinimalUser.from(entity)))
                .catch(reason => this.errorService.convertError(reason));
        }
    }

    public searchByName(name: string): Promise<User[]> {
        return this.securityService.getRequest<UserResource[]>(`${environment.registrationUrl}/search/user/name/${name}`)
            .then(value => value.body.map(user => this.convert(user)))
            .catch(reason => this.errorService.convertError(reason));
    }

    protected buildFetchUrlById(id: number): string {
        return `${environment.registrationUrl}/user/${id}`;
    }

    protected buildFetchUrlByIds(): string {
        return `${environment.registrationUrl}/users/ids`;
    }

    protected buildEntityPageFetchUrl(pageSize: number, offset: number): string {
        return `${environment.registrationUrl}/users/page?pageSize=${pageSize}&offset=${offset}`;
    }

    protected convert(user: UserResource): User {
        return User.from(user);
    }
}
