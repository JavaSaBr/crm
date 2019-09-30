import {Injectable} from '@angular/core';
import {environment} from '@app/env/environment';
import {ErrorResponse} from '@app/error/error-response';
import {Contact} from '@app/entity/contact';
import {ContactResource} from '@app/resource/contact-resource';
import {SecurityService} from '@app/service/security.service';
import {TranslateService} from '@ngx-translate/core';
import {DatePipe} from '@angular/common';
import {ContactPhoneNumberResource} from '@app/resource/contact-phone-number-resource';
import {ContactEmailResource} from '@app/resource/contact-email-resource';
import {ContactSiteResource} from '@app/resource/contact-site-resource';
import {ContactMessengerResource} from '@app/resource/contact-messenger-resource';
import {CountryRepository} from '@app/repository/country/country.repository';
import {ContactSite, SiteType} from '@app/entity/contact-site';
import {ContactMessenger, MessengerType} from '@app/entity/contact-messenger';
import {ContactEmail, EmailType} from '@app/entity/contact-email';
import {AsyncEntityRemoteRepository} from '@app/repository/async-entity-remote.repository';
import {PhoneNumber} from '@app/entity/phone-number';
import {ContactPhoneNumber, PhoneNumberType} from '@app/entity/contact-phone-number';

@Injectable({
    providedIn: 'root'
})
export class ContactRepository extends AsyncEntityRemoteRepository<Contact, ContactResource> {

    protected constructor(
        private readonly countryRepository: CountryRepository,
        private readonly datePipe: DatePipe,
        securityService: SecurityService,
        translateService: TranslateService,
    ) {
        super(securityService, translateService)
    }

    create(contact: Contact): Promise<ErrorResponse | Contact> {

        let body = new ContactResource();
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
        return this.securityService.postRequest<ContactResource>(url, body)
            .then(resp => this.convertAsync(resp.body))
            .catch(error => {
                return ErrorResponse.convertToErrorOrNull(error, this.translateService);
            });
    }

    protected buildFetchUrl(): string {
        return `${environment.clientUrl}/contacts`;
    }

    protected buildFetchUrlById(id: number): string {
        return `${environment.clientUrl}/contact/${id}`;
    }

    protected convertAsync(resource: ContactResource): Promise<Contact> {

        const contact = Contact.create();
        contact.id = resource.id;
        contact.assigner = resource.assigner;
        contact.curators = resource.curators;
        contact.firstName = resource.firstName;
        contact.secondName = resource.secondName;
        contact.thirdName = resource.thirdName;
        contact.company = resource.company;
        contact.birthday = resource.birthday ? new Date(resource.birthday) : null;
        contact.sites = resource.sites
            .map(value => new ContactSite(value.url, value.type as SiteType));
        contact.messengers = resource.messengers
            .map(value => new ContactMessenger(value.login, value.type as MessengerType));
        contact.emails = resource.emails
            .map(value => new ContactEmail(value.email, value.type as EmailType));

        const phoneNumberResources = resource.phoneNumbers;

        if (!phoneNumberResources || phoneNumberResources.length < 1) {
            contact.phoneNumbers = [];
            return Promise.resolve(contact);
        }

        let asyncPhoneNumbers: Promise<ContactPhoneNumber>[] = [];

        phoneNumberResources.forEach(resource => {
            if (!resource.countryCode) {
                const phoneNumber = new PhoneNumber(null, resource.regionCode, resource.phoneNumber);
                const contactPhoneNumber = new ContactPhoneNumber(phoneNumber, resource.type as PhoneNumberType);
                asyncPhoneNumbers.push(Promise.resolve(contactPhoneNumber));
            } else {
                asyncPhoneNumbers.push(this.countryRepository.findByPhoneCode(resource.countryCode)
                    .then(country => new PhoneNumber(country, resource.regionCode, resource.phoneNumber))
                    .then(phoneNumber => new ContactPhoneNumber(phoneNumber, resource.type as PhoneNumberType)));
            }
        });

        return Promise.all(asyncPhoneNumbers)
            .then(phoneNumbers => {
                contact.phoneNumbers = phoneNumbers;
                return contact;
            })
    }
}

