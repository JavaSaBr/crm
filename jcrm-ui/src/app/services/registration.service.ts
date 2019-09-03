import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {PhoneNumber} from '../input/phone-number/phone-number';
import {Country} from '../entity/country';
import {ErrorResponse} from '../error/error-response';
import {TranslateService} from '@ngx-translate/core';
import {OrganizationRegisterOutResource} from '../resources/organization-register-out-resource';
import {AuthenticationInResource} from '../resources/authentication-in-resource';
import {AuthenticationOutResource} from '../resources/authentication-out-resource';
import {HttpClient} from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class RegistrationService {

    static readonly ERROR_EXPIRED_TOKEN = 1011;

    constructor(
        private readonly translateService: TranslateService,
        private readonly httpClient: HttpClient,
    ) {
    }

    register(
        orgName: string,
        country: Country,
        firstName: string,
        secondName: string,
        email: string,
        activationCode: string,
        password: string,
        phoneNumber: PhoneNumber,
        subscribe: boolean
    ): Promise<AuthenticationInResource> {

        let body = new OrganizationRegisterOutResource(
            orgName,
            email,
            activationCode,
            password,
            firstName,
            secondName,
            phoneNumber.country.phoneCode + phoneNumber.phoneNumber,
            subscribe,
            country.id
        );

        return new Promise<AuthenticationInResource>((resolve, reject) => {
            const url = environment.registrationUrl + '/register/organization';
            this.httpClient.post<AuthenticationInResource>(url, body, {observe: 'response'})
                .toPromise()
                .then(resp => resolve(resp.body))
                .catch(resp => reject(ErrorResponse.convertToErrorOrNull(resp, this.translateService)));
        });
    }

    authenticate(
        login: string,
        password: string
    ): Promise<AuthenticationInResource> {

        let body = new AuthenticationOutResource(login, password);

        return new Promise<AuthenticationInResource>((resolve, reject) => {
            const url = environment.registrationUrl + '/authenticate';
            this.httpClient.post<AuthenticationInResource>(url, body, {observe: 'response'})
                .toPromise()
                .then(resp => resolve(resp.body))
                .catch(resp => reject(ErrorResponse.convertToErrorOrNull(resp, this.translateService)));
        });
    }

    authenticateByToken(token: string): Promise<AuthenticationInResource> {
        return new Promise<AuthenticationInResource>((resolve, reject) => {
            const url = environment.registrationUrl + '/authenticate/' + token;
            this.httpClient.get<AuthenticationInResource>(url, {observe: 'response'})
                .toPromise()
                .then(resp => resolve(resp.body))
                .catch(resp => reject(ErrorResponse.convertToErrorOrNull(resp, this.translateService)));
        });
    }

    refreshToken(token: string): Promise<AuthenticationInResource> {
        return new Promise<AuthenticationInResource>((resolve, reject) => {
            const url = environment.registrationUrl + '/token/refresh/' + token;
            this.httpClient.get<AuthenticationInResource>(url, {observe: 'response'})
                .toPromise()
                .then(resp => resolve(resp.body))
                .catch(resp => reject(ErrorResponse.convertToErrorOrNull(resp, this.translateService)));
        });
    }

    confirmEmail(email: string): Promise<ErrorResponse | null> {
        const url = environment.registrationUrl + '/email-confirmation/' + email;
        return this.httpClient.get(url, {observe: 'response'})
            .toPromise()
            .then(() => null)
            .catch(resp => ErrorResponse.convertToErrorOrNull(resp, this.translateService));
    }

    orgExistByName(orgName: string): Promise<boolean> {
        const url = environment.registrationUrl + '/exist/organization/name/' + orgName;
        return this.httpClient.get<{}>(url, {observe: 'response'})
            .toPromise()
            .then(value => value.ok)
            .catch(() => false);
    }

    userExistByName(name: string): Promise<boolean> {
        const url = environment.registrationUrl + '/exist/user/name/' + name;
        return this.httpClient.get<{}>(url, {observe: 'response'})
            .toPromise()
            .then(value => value.ok)
            .catch(() => false);
    }
}
