import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class SecurityService {

    private accessToken: string;

    constructor(private httpClient: HttpClient) {
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
