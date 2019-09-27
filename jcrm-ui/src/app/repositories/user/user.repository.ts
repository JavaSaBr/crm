import {Injectable} from '@angular/core';
import {environment} from '@app/env/environment';
import {User} from '@app/entity/user';
import {RemoteRepository} from '@app/repository/remote.repository';

@Injectable({
    providedIn: 'root'
})
//FIXME update implementation
export class UserRepository extends RemoteRepository<User, User> {

    protected buildFetchUrl(): string {
        return `${environment.registrationUrl}/users`;
    }

    protected buildFetchUrlById(id: number): string {
        return `${environment.registrationUrl}/user/${id}`;
    }

    protected buildFetchUrlByIds(): string {
        return `${environment.registrationUrl}/users/ids`;
    }

    public searchByName(name: string): Promise<User[]> {
        return this.securityService.getRequest<User[]>(`${environment.registrationUrl}/search/user/name/${name}`)
            .then(value => value.body.map(user => this.convert(user)));
    }

    protected convert(user: User): User {
        return new User(user);
    }
}
