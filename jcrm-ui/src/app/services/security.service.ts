import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Observable, Subject} from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class SecurityService {

    private readonly _authenticated: Subject<boolean>;
    private accessToken: string;

    constructor(private httpClient: HttpClient) {
        this._authenticated = new Subject<boolean>();
        this._authenticated.next(false);
    }

    authenticate(token: string) {
        this.accessToken = token;
        this._authenticated.next(this.isAuthenticated());
    }

    isAuthenticated() {
        return this.accessToken != null && this.accessToken.length > 0;
    }

    get authenticated(): Observable<boolean> {
        return this._authenticated;
    }

    postRequest<T>(url: string, body: any | null): Promise<HttpResponse<T>> {

        const headers = new HttpHeaders();

        if (this.accessToken) {
            headers.append('token', this.accessToken);
        }

        return this.httpClient.post(url, body, {
                headers: headers,
                observe: 'response'
            })
            .toPromise() as Promise<HttpResponse<T>>;
    }

    getRequest<T>(url: string): Promise<HttpResponse<T>> {

        const headers = new HttpHeaders();

        if (this.accessToken) {
            headers.append('token', this.accessToken);
        }

        return this.httpClient.get(url, {
                headers: headers,
                observe: 'response'
            })
            .toPromise() as Promise<HttpResponse<T>>;
    }
}
