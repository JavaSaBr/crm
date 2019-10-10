import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from '@angular/common/http';
import {BehaviorSubject, Observable, Subject} from 'rxjs';
import {User} from '@app/entity/user';
import {RegistrationService} from '@app/service/registration.service';
import {ErrorResponse} from '@app/error/error-response';

@Injectable({
    providedIn: 'root'
})
export class SecurityService {

    public static readonly MAX_REFRESHED_TOKEN_MESSAGE = 2001;

    private static readonly LOCAL_STORAGE_TOKEN = 'jcrm:auth:token';
    private static readonly HEADER_TOKEN = 'JCRM-Access-Token';

    private readonly _authenticated: Subject<boolean>;
    private readonly _userObservable: BehaviorSubject<User | null>;

    private _accessToken: string | null;

    constructor(
        private readonly httpClient: HttpClient,
        private readonly registrationService: RegistrationService
    ) {
        this._accessToken = '';
        this._userObservable = new BehaviorSubject<User | null>(null);
        this._authenticated = new Subject<boolean>();
    }

    private internalAuthenticate(user: User | null, token: string | null) {
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

        return this.registrationService.authenticateByToken(token)
            .then(resource => {
                this.authenticate(resource.user, resource.token);
                return true;
            })
            .catch(reason => {

                if (!ErrorResponse.isTokenExpired(reason)) {
                    return false;
                }

                return this.registrationService.refreshToken(token)
                    .then(value => {
                        this.authenticate(value.user, value.token);
                        return true;
                    })
                    .catch(() => {
                        this.logout();
                        return false;
                    });
            });
    }

    authenticate(user: User, token: string): void {
        this.internalAuthenticate(user, token);

        if (token) {
            localStorage.setItem(SecurityService.LOCAL_STORAGE_TOKEN, token);
        }
    }

    get userObservable(): Observable<User | null> {
        return this._userObservable;
    }

    get currentUser(): User | null {
        return this._userObservable.value;
    }

    isAuthenticated(): boolean {
        return this._accessToken && this._accessToken.length > 0;
    }

    get authenticated(): Observable<boolean> {
        return this._authenticated;
    }

    postRequest<T>(url: string, body: any | null): Promise<HttpResponse<T>> {

        let headers = new HttpHeaders();

        if (this._accessToken) {
            headers = headers.append(SecurityService.HEADER_TOKEN, this._accessToken);
        }

        const asyncRequest = this.httpClient.post(url, body, {
            headers: headers,
            observe: 'response'
        }).toPromise() as Promise<HttpResponse<T>>;

        return asyncRequest
            .then(value => value)
            .catch(reason => this.handleExpiredToken<T>(reason, () => this.postRequest(url, body)));
    }

    private handleExpiredToken<T>(reason: any, callback: () => Promise<HttpResponse<T>>) {

        if (!ErrorResponse.isTokenExpired(reason)) {
            return Promise.reject(reason);
        }

        return this.registrationService.refreshToken(this._accessToken)
            .then(value => {
                this.authenticate(value.user, value.token);
                return callback();
            })
            .catch(() => {
                this.logout();
                return Promise.reject(reason);
            });
    }

    putRequest<T>(url: string, body: any | null): Promise<HttpResponse<T>> {

        let headers = new HttpHeaders();

        if (this._accessToken) {
            headers = headers.append(SecurityService.HEADER_TOKEN, this._accessToken);
        }

        const asyncRequest = this.httpClient.put(url, body, {
            headers: headers,
            observe: 'response'
        }).toPromise() as Promise<HttpResponse<T>>;

        return asyncRequest
            .then(value => value)
            .catch(reason => this.handleExpiredToken<T>(reason, () => this.putRequest(url, body)));
    }

    postUnauthorizedRequest<T>(url: string, body: any | null): Promise<HttpResponse<T>> {

        return this.httpClient.post(url, body, {
            observe: 'response'
        }).toPromise() as Promise<HttpResponse<T>>;
    }

    getRequest<T>(url: string): Promise<HttpResponse<T>> {

        let headers = new HttpHeaders();

        if (this._accessToken) {
            headers = headers.append(SecurityService.HEADER_TOKEN, this._accessToken);
        }

        const asyncRequest = this.httpClient.get(url, {
            headers: headers,
            observe: 'response'
        }).toPromise() as Promise<HttpResponse<T>>;

        return asyncRequest
            .then(value => value)
            .catch(reason => this.handleExpiredToken<T>(reason, () => this.getRequest(url)));
    }

    getUnauthorizedRequest<T>(url: string, params?: HttpParams): Promise<HttpResponse<T>> {

        return this.httpClient.get(url, {
            params: params,
            observe: 'response'
        }).toPromise() as Promise<HttpResponse<T>>;
    }
}
