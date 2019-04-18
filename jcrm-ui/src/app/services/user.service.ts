import {Injectable} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {User} from '../entity/user';
import {SecurityService} from './security.service';

@Injectable({
    providedIn: 'root'
})
export class UserService {

    private readonly _currentUser: Subject<User>;

    constructor(private readonly securityService: SecurityService) {
        this._currentUser = new Subject<User>();
    }

    authenticate(user: User, token: string) {
        this._currentUser.next(user);
        this.securityService.authenticate(token);
    }

    get currentUser(): Observable<User> {
        return this._currentUser;
    }
}
