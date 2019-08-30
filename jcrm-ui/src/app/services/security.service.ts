import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {User} from '@app/entity/user';
import {RegistrationService} from '@app/service/registration.service';
import {ErrorResponse} from '@app/error/error-response';

@Injectable({
    providedIn: 'root'
})
export class SecurityService {

    private static readonly LOCAL_STORAGE_TOKEN = 'jcrm:auth:token';
    private static readonly HEADER_TOKEN = 'JCRM-Access-Token';

    private readonly _authenticated: BehaviorSubject<boolean>;
    private readonly _userObservable: BehaviorSubject<User | null>;

    private _accessToken: string | null;

    constructor(
        private readonly httpClient: HttpClient,
        private readonly registrationService: RegistrationService
    ) {
        this._accessToken = '';
        this._userObservable = new BehaviorSubject<User | null>(null);
        this._authenticated = new BehaviorSubject<boolean>(false);
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

        return new Promise(resolve => {

            this.registrationService.authenticateByToken(token)
                .then(value => {
                    this.authenticate(value.user, value.token);
                    resolve(true);
                })
                .catch(reason => {

                    let error = reason as ErrorResponse;

                    if (error.errorCode != RegistrationService.ERROR_EXPIRED_TOKEN) {
                        this.logout();
                        resolve(false);
                        return;
                    }

                    this.registrationService.refreshToken(token)
                        .then(value => {
                            this.authenticate(value.user, value.token);
                            resolve(true);
                        })
                        .catch(() => {
                            this.logout();
                            resolve(false);
                        });
                });
        });
    }

    authenticate(user: User, token: string) {
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

        let headers = new HttpHeaders();

        if (this._accessToken) {
            headers = headers.append(SecurityService.HEADER_TOKEN, this._accessToken);
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
