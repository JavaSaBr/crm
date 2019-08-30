import {Injectable} from '@angular/core';
import {environment} from '@app/env/environment';
import {ErrorResponse} from '@app/error/error-response';
import {TranslateService} from '@ngx-translate/core';
import {HttpClient} from '@angular/common/http';
import {SecurityService} from '@app/service/security.service';
import {Contact} from '@app/entity/contact';
import {ContactsInResource} from '@app/resource/contacts-in-resource';
import {CreateContactOutResource} from '@app/resource/create-contact-out-resource';

@Injectable({
    providedIn: 'root'
})
export class ContactService {

    constructor(
        private readonly translateService: TranslateService,
        private readonly httpClient: HttpClient,
        private readonly securityService: SecurityService
    ) {
    }

    create(
        fistName: string,
        secondName: string,
        thirdName: string
    ): Promise<Contact> {

        let body = new CreateContactOutResource(
            fistName,
            secondName,
            thirdName
        );

        return new Promise<Contact>((resolve, reject) => {
            const url = environment.clientUrl + '/contact/create';
            this.securityService.postRequest<Contact>(url, body)
                .then(resp => resolve(resp.body))
                .catch(resp => reject(ErrorResponse.convertToErrorOrNull(resp, this.translateService)));
        });
    }

    getContacts(): Promise<Contact[]> {
        return new Promise<Contact[]>((resolve, reject) => {
            const url = environment.clientUrl + '/contacts';
            this.securityService.getRequest<ContactsInResource>(url)
                .then(resp => resolve(resp.body.contacts))
                .catch(resp => reject(ErrorResponse.convertToErrorOrNull(resp, this.translateService)));
        });
    }
}

