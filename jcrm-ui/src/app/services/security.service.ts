import {Injectable, Injector, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Observable, Subject} from 'rxjs';
import {User} from '../entity/user';

@Injectable({
    providedIn: 'root'
})
export class SecurityService {

    private static readonly LOCAL_STORAGE_TOKEN = 'jcrm:auth:token';

    private readonly _authenticated: Subject<boolean>;
    private readonly _userObservable: Subject<User>;

    private _accessToken: string;
    private _currentUser: User;

    constructor(
        private readonly httpClient: HttpClient,
        private readonly injector: Injector
    ) {
        this._userObservable = new Subject<User>();
        this._userObservable.subscribe(user => this._currentUser = user);
        this._authenticated = new Subject<boolean>();
        this._authenticated.next(false);
    }

    tryToRestoreToken(): Promise<boolean> {

        //TODO
        let token = localStorage.getItem(SecurityService.LOCAL_STORAGE_TOKEN);

        if (token) {
            this.internalAuthenticate(null, token);
            return Promise.resolve(true)
        }

        //let registrationService = this.injector.get(RegistrationService);
        return Promise.resolve(false)
    }

    private internalAuthenticate(user: User, token: string) {
        this._accessToken = token;
        this._userObservable.next(user);
        this._authenticated.next(this.isAuthenticated());
    }

    authenticate(user: User, token: string) {
        this.internalAuthenticate(user, token)

        if (token) {
            localStorage.setItem(SecurityService.LOCAL_STORAGE_TOKEN, token);
        }
    }

    get userObservable(): Observable<User> {
        return this._userObservable;
    }

    get currentUser(): User | null {
        return this._currentUser;
    }

    isAuthenticated() {
        return this._accessToken != null && this._accessToken.length > 0;
    }

    get authenticated(): Observable<boolean> {
        return this._authenticated;
    }

    postRequest<T>(url: string, body: any | null): Promise<HttpResponse<T>> {

        const headers = new HttpHeaders();

        if (this._accessToken) {
            headers.append('token', this._accessToken);
        }

        return this.httpClient.post(url, body, {
            headers: headers,
            observe: 'response'
        }).toPromise() as Promise<HttpResponse<T>>;
    }

    getRequest<T>(url: string): Promise<HttpResponse<T>> {

        const headers = new HttpHeaders();

        if (this._accessToken) {
            headers.append('token', this._accessToken);
        }

        return this.httpClient.get(url, {
            headers: headers,
            observe: 'response'
        }).toPromise() as Promise<HttpResponse<T>>;
    }
}
