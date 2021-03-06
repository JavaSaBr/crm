import {Injectable} from '@angular/core';
import {environment} from '@app/env/environment';
import {User} from '@app/entity/user';
import {RemoteRepository} from '@app/repository/remote.repository';
import {MinimalUser} from '@app/entity/minimal-user';
import {MinimalUserResource} from '@app/resource/minimal-user-resource';
import {UserResource} from '@app/resource/user-resource';
import {PhoneNumberResource} from '@app/resource/phone-number-resource';
import {MessengerResource} from '@app/resource/messenger-resource';
import {SecurityService} from '@app/service/security.service';
import {ErrorService} from '@app/service/error.service';
import {DatePipe} from '@app/node-modules/@angular/common';

@Injectable({
    providedIn: 'root'
})
export class UserRepository extends RemoteRepository<User, UserResource> {

    constructor(
        private readonly datePipe: DatePipe,
        securityService: SecurityService,
        errorService: ErrorService)
    {
        super(securityService, errorService);
    }

    public create(user: User): Promise<User> {

        const body = this.convertToResource(user);
        const url = `${environment.registrationUrl}/user`;

        return this.securityService.postRequest<UserResource>(url, body)
            .then(response => this.convertFromResource(response.body))
            .catch(reason => this.errorService.convertError(reason));
    }

    public findMinimalById(id: number): Promise<MinimalUser | null> {
        return this.securityService.getRequest<MinimalUserResource>(`${environment.registrationUrl}/user/minimal/${id}`)
            .then(response => MinimalUserResource.toMinimalUser(response.body))
            .catch(reason => this.errorService.convertError(reason));
    }

    public findMinimalByIds(ids: number[]): Promise<MinimalUser[]> {
        if (ids.length < 1) {
            return Promise.resolve([]);
        } else {
            return this.securityService.postRequest<MinimalUserResource[]>(`${environment.registrationUrl}/users/minimal/ids`, ids)
                .then(response => response.body.map(resource => MinimalUserResource.toMinimalUser(resource)))
                .catch(reason => this.errorService.convertError(reason));
        }
    }

    public searchByName(name: string): Promise<MinimalUser[]> {
        return this.securityService.getRequest<MinimalUserResource[]>(`${environment.registrationUrl}/search/user/name/${name}`)
            .then(value => value.body.map(resource => MinimalUserResource.toMinimalUser(resource)))
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

    protected convertFromResource(resource: UserResource): User {
        return new User(
            resource.id,
            resource.email,
            resource.firstName,
            resource.secondName,
            resource.thirdName,
            resource.birthday ? new Date(resource.birthday) : null,
            resource.phoneNumbers
                .map(value => PhoneNumberResource.toPhoneNumber(value)),
            resource.messengers
                .map(value => MessengerResource.toMessenger(value)),
            resource.password,
            resource.created ? new Date(resource.created) : null,
            resource.modified ? new Date(resource.modified) : null
        );
    }

    private convertToResource(user: User): UserResource {
        return new UserResource(
            user.id,
            user.email,
            user.firstName,
            user.secondName,
            user.thirdName,
            this.datePipe.transform(user.birthday, 'yyyy-MM-dd'),
            user.phoneNumbers ? user.phoneNumbers.map(value => PhoneNumberResource.of(value)) : null,
            user.messengers ? user.messengers.map(value => MessengerResource.of(value)) : null,
            user.password,
            user.created ? user.created.getTime() : null,
            user.modified ? user.modified.getTime() : null
        );
    }
}
