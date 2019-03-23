import {Injectable} from '@angular/core';
import {SecurityService} from './security.service';
import {environment} from '../../environments/environment';
import {PhoneNumber} from '../input/phone-number/phone-number';
import {Country} from '../entity/country';
import {ErrorResponse} from '../utils/ErrorResponse';

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
        thirdName: string,
        email: string,
        password: string,
        phoneNumber: PhoneNumber,
        subscribe: boolean
    ): Promise<number | null> {

        return this.securityService.postRequest(environment.registrationUrl + '/organization/register', {
                orgName: orgName,
                countryId: country.id,
                firstName: firstName,
                secondName: secondName,
                thirdName: thirdName,
                email: email,
                password: password,
                phoneNumber: phoneNumber.country.phoneCode + phoneNumber.phoneNumber,
                subscribe: subscribe
            })
            .then(resp => {

                if (resp.ok) {
                    return null;
                } else if (resp.status == 400) {
                    const body = resp.body as ErrorResponse;
                    return body.errorCode;
                }

                return resp.statusText;
            })
            .catch(reason => reason);
    }

    exist(orgName: string): Promise<boolean> {
        return this.securityService.getRequest(environment.registrationUrl + '/organization/exist/' + orgName)
            .then(value => value.ok)
            .catch(() => false);
    }
}
