import {Injectable, Injector, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from '@angular/common/http';
import {Observable, Subject} from 'rxjs';
import {User} from '../entity/user';
import {RegistrationService} from './registration.service';
import {ErrorResponse} from '../error/error-response';

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
        private readonly registrationService: RegistrationService
    ) {
        this._userObservable = new Subject<User>();
        this._userObservable.subscribe(user => this._currentUser = user);
        this._authenticated = new Subject<boolean>();
        this._authenticated.next(false);
    }

    private internalAuthenticate(user: User, token: string) {
        this._accessToken = token;
        this._userObservable.next(user);
        this._authenticated.next(this.isAuthenticated());
    }

    logout() {
        localStorage.removeItem(SecurityService.LOCAL_STORAGE_TOKEN);
        this.internalAuthenticate(null, null);
    }

    tryToRestoreToken(): Promise<boolean> {

        let token = localStorage.getItem(SecurityService.LOCAL_STORAGE_TOKEN);

        if (!token) {
            return Promise.resolve(false);
        }

        return new Promise(resolve => {

            this.registrationService.authenticateByToken(token)
                .then(value => {
                    this.internalAuthenticate(value.user, value.token);
                    resolve(true);
                })
                .catch(reason => {

                    let error = reason as ErrorResponse;

                    if (error.errorCode != RegistrationService.ERROR_EXPIRED_TOKEN) {
                        resolve(false);
                        return;
                    }

                    this.registrationService.refreshToken(token)
                        .then(value => {
                            this.internalAuthenticate(value.user, value.token);
                            resolve(true);
                        })
                        .catch(() => resolve(false))
                });
        })
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

    postUnauthorizedRequest<T>(url: string, body: any | null): Promise<HttpResponse<T>> {

        return this.httpClient.post(url, body, {
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

    getUnauthorizedRequest<T>(url: string, params?: HttpParams): Promise<HttpResponse<T>> {

        return this.httpClient.get(url, {
            params: params,
            observe: 'response'
        }).toPromise() as Promise<HttpResponse<T>>;
    }
}
