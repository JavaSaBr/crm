import {Injectable} from '@angular/core';
import {environment} from '@app/env/environment';
import {ErrorResponse} from '@app/error/error-response';
import {Contact} from '@app/entity/contact';
import {ContactOutResource} from '@app/resource/contact-out-resource';
import {RemoteRepository} from '@app/repository/remote.repository';
import {SecurityService} from '@app/service/security.service';
import {TranslateService} from '@ngx-translate/core';
import {DatePipe} from '@angular/common';
import {ContactPhoneNumberResource} from '@app/resource/contact-phone-number-resource';
import {ContactEmailResource} from '@app/resource/contact-email-resource';
import {ContactSiteResource} from '@app/resource/contact-site-resource';
import {ContactMessengerResource} from '@app/resource/contact-messenger-resource';

@Injectable({
    providedIn: 'root'
})
export class ContactRepository extends RemoteRepository<Contact> {

    protected constructor(
        securityService: SecurityService,
        translateService: TranslateService,
        private readonly datePipe: DatePipe,
    ) {
        super(securityService, translateService)
    }

    create(contact: Contact): Promise<Contact | null> {

        let body = new ContactOutResource();
        body.assigner = contact.assigner;
        body.curators = contact.curators;
        body.firstName = contact.firstName;
        body.secondName = contact.secondName;
        body.thirdName = contact.thirdName;
        body.company = contact.company;
        body.birthday = this.datePipe.transform(contact.birthday, 'yyyy-MM-dd');
        body.phoneNumbers = contact.phoneNumbers.map(value => ContactPhoneNumberResource.valueOf(value));
        body.emails = contact.emails.map(value => ContactEmailResource.valueOf(value));
        body.sites = contact.sites.map(value => ContactSiteResource.valueOf(value));
        body.messengers = contact.messengers.map(value => ContactMessengerResource.valueOf(value));

        const url = `${environment.clientUrl}/contact/create`;
        return this.securityService.postRequest<Contact>(url, body)
            .then(resp => this.convert(resp.body))
            .catch(resp => {
                ErrorResponse.convertToErrorOrNull(resp, this.translateService);
                return null;
            });
    }

    protected buildFetchUrl(): string {
        return `${environment.clientUrl}/contacts`;
    }

    protected buildFetchUrlById(id: number): string {
        return `${environment.clientUrl}/contact/${id}`;
    }

    protected convert(contact: Contact): Contact {
        return Contact.create(contact);
    }
}

