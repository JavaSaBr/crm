import {Injectable} from '@angular/core';
import {SecurityService} from './security.service';
import {environment} from '../../environments/environment';
import {PhoneNumber} from '../input/phone-number/phone-number';
import {Country} from '../entity/country';
import {ErrorResponse} from '../error/error-response';

@Injectable({
    providedIn: 'root'
})
export class RegistrationService {

    constructor(private securityService: SecurityService) {
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
    ): Promise<ErrorResponse | null> {

        return this.securityService.postRequest(environment.registrationUrl + '/register/organization', {
                orgName: orgName,
                countryId: country.id,
                firstName: firstName,
                secondName: secondName,
                email: email,
                activationCode: activationCode,
                password: password,
                phoneNumber: phoneNumber.country.phoneCode + phoneNumber.phoneNumber,
                subscribe: subscribe
            })
            .then(() => null)
            .catch(resp => ErrorResponse.convertToErrorOrNull(resp));
    }

    confirmEmail(email: string): Promise<ErrorResponse | null> {

        return this.securityService.getRequest(environment.registrationUrl + '/email/confirmation/' + email)
            .then(() => null)
            .catch(resp => ErrorResponse.convertToErrorOrNull(resp));
    }

    orgExistByName(orgName: string): Promise<boolean> {
        return this.securityService.getRequest(environment.registrationUrl + '/exist/organization/name/' + orgName)
            .then(value => value.ok)
            .catch(() => false);
    }

    userExistByName(name: string): Promise<boolean> {
        return this.securityService.getRequest(environment.registrationUrl + '/exist/user/name/' + name)
            .then(value => value.ok)
            .catch(() => false);
    }
}
