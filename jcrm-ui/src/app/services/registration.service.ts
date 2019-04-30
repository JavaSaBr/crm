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
            this.httpClient.post<AuthenticationInResource>(environment.registrationUrl + '/register/organization', body, {observe: 'response'})
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
            this.httpClient.post<AuthenticationInResource>(environment.registrationUrl + '/authenticate', body, {observe: 'response'})
                .toPromise()
                .then(resp => resolve(resp.body))
                .catch(resp => reject(ErrorResponse.convertToErrorOrNull(resp, this.translateService)));
        });
    }

    authenticateByToken(token: string): Promise<AuthenticationInResource> {

        return new Promise<AuthenticationInResource>((resolve, reject) => {
            this.httpClient.get<AuthenticationInResource>(environment.registrationUrl + '/authenticate/' + token, {observe: 'response'})
                .toPromise()
                .then(resp => resolve(resp.body))
                .catch(resp => reject(ErrorResponse.convertToErrorOrNull(resp, this.translateService)));
        });
    }

    refreshToken(token: string): Promise<AuthenticationInResource> {

        return new Promise<AuthenticationInResource>((resolve, reject) => {
            this.httpClient.get<AuthenticationInResource>(environment.registrationUrl + '/token/refresh/' + token, {observe: 'response'})
                .toPromise()
                .then(resp => resolve(resp.body))
                .catch(resp => reject(ErrorResponse.convertToErrorOrNull(resp, this.translateService)));
        });
    }

    confirmEmail(email: string): Promise<ErrorResponse | null> {

        return this.httpClient.get(environment.registrationUrl + '/email/confirmation/' + email, {observe: 'response'})
            .toPromise()
            .then(() => null)
            .catch(resp => ErrorResponse.convertToErrorOrNull(resp, this.translateService));
    }

    orgExistByName(orgName: string): Promise<boolean> {
        return this.httpClient.get<{}>(environment.registrationUrl + '/exist/organization/name/' + orgName, {observe: 'response'})
            .toPromise()
            .then(value => value.ok)
            .catch(() => false);
    }

    userExistByName(name: string): Promise<boolean> {
        return this.httpClient.get<{}>(environment.registrationUrl + '/exist/user/name/' + name, {observe: 'response'})
            .toPromise()
            .then(value => value.ok)
            .catch(() => false);
    }
}
