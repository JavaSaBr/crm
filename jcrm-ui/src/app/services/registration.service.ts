import {Injectable} from '@angular/core';
import {SecurityService} from './security.service';
import {environment} from '../../environments/environment';
import {PhoneNumber} from '../input/phone-number/phone-number';
import {Country} from '../entity/country';
import {ErrorResponse} from '../error/error-response';
import {TranslateService} from '@ngx-translate/core';
import {OrganizationRegisterOutResource} from '../resources/organization-register-out-resource';
import {AuthenticationInResource} from '../resources/authentication-in-resource';
import {AuthenticationOutResource} from '../resources/authentication-out-resource';

@Injectable({
    providedIn: 'root'
})
export class RegistrationService {

    constructor(
        private securityService: SecurityService,
        private translateService: TranslateService
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
            this.securityService.postRequest<AuthenticationInResource>(environment.registrationUrl + '/register/organization', body)
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
            this.securityService.postRequest<AuthenticationInResource>(environment.registrationUrl + '/authenticate', body)
                .then(resp => resolve(resp.body))
                .catch(resp => reject(ErrorResponse.convertToErrorOrNull(resp, this.translateService)));
        });
    }

    confirmEmail(email: string): Promise<ErrorResponse | null> {

        return this.securityService.getRequest(environment.registrationUrl + '/email/confirmation/' + email)
            .then(() => null)
            .catch(resp => ErrorResponse.convertToErrorOrNull(resp, this.translateService));
    }

    orgExistByName(orgName: string): Promise<boolean> {
        return this.securityService.getRequest<{}>(environment.registrationUrl + '/exist/organization/name/' + orgName)
            .then(value => value.ok)
            .catch(() => false);
    }

    userExistByName(name: string): Promise<boolean> {
        return this.securityService.getRequest<{}>(environment.registrationUrl + '/exist/user/name/' + name)
            .then(value => value.ok)
            .catch(() => false);
    }
}
