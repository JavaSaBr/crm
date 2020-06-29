import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {PhoneNumber} from '@app/entity/phone-number';
import {Country} from '../entity/country';
import {ErrorResponse} from '../error/error-response';
import {TranslateService} from '@ngx-translate/core';
import {OrganizationRegisterOutResource} from '../resources/organization-register-out-resource';
import {AuthenticationInResource} from '../resources/authentication-in-resource';
import {AuthenticationOutResource} from '../resources/authentication-out-resource';
import {HttpClient} from '@angular/common/http';
import {ErrorService} from '@app/service/error.service';
import {PhoneNumberResource} from '@app/resource/phone-number-resource';

@Injectable({
    providedIn: 'root'
})
export class RegistrationService {

    constructor(
        private readonly errorService: ErrorService,
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

        const url = `${environment.registrationUrl}/register/organization`;
        const body = new OrganizationRegisterOutResource(
            orgName,
            email,
            activationCode,
            password,
            firstName,
            secondName,
            PhoneNumberResource.of(phoneNumber),
            subscribe,
            country.id
        );

        return this.httpClient.post<AuthenticationInResource>(url, body, {observe: 'response'})
            .toPromise()
            .then(response => AuthenticationInResource.of(response.body))
            .catch(reason => this.errorService.convertError(reason));
    }

    authenticate(login: string, password: string): Promise<AuthenticationInResource> {

        const url = `${environment.registrationUrl}/authenticate`;
        const body = new AuthenticationOutResource(login, password);

        return this.httpClient.post<AuthenticationInResource>(url, body, {observe: 'response'})
            .toPromise()
            .then(response => AuthenticationInResource.of(response.body))
            .catch(reason => this.errorService.convertError(reason));
    }

    authenticateByToken(token: string): Promise<AuthenticationInResource> {

        const url = `${environment.registrationUrl}/authenticate/${token}`;

        return this.httpClient.get<AuthenticationInResource>(url, {observe: 'response'})
            .toPromise()
            .then(response => AuthenticationInResource.of(response.body))
            .catch(reason => this.errorService.convertError(reason));
    }

    refreshToken(token: string): Promise<AuthenticationInResource> {

        const url = `${environment.registrationUrl}/token/refresh/${token}`;

        return this.httpClient.get<AuthenticationInResource>(url, {observe: 'response'})
            .toPromise()
            .then(response => AuthenticationInResource.of(response.body))
            .catch(reason => this.errorService.convertError(reason));
    }

    confirmEmail(email: string): Promise<void> {
        const url = `${environment.registrationUrl}/email-confirmation/${email}`;
        return this.httpClient.get<{}>(url, {observe: 'response'})
            .toPromise()
            .then(() => null)
            .catch(reason => this.errorService.convertError(reason));
    }

    orgExistByName(orgName: string): Promise<boolean> {
        const url = `${environment.registrationUrl}/exist/organization/name/${orgName}`;
        return this.httpClient.get<{}>(url, {observe: 'response'})
            .toPromise()
            .then(value => value.ok)
            .catch(() => false);
    }

    userExistByName(name: string): Promise<boolean> {
        const url = `${environment.registrationUrl}/exist/user/name/${name}`;
        return this.httpClient.get<{}>(url, {observe: 'response'})
            .toPromise()
            .then(value => value.ok)
            .catch(() => false);
    }
}
