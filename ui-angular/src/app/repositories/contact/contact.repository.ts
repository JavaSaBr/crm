import {Injectable} from '@angular/core';
import {environment} from '@app/env/environment';
import {Contact} from '@app/entity/contact';
import {ContactResource} from '@app/resource/contact-resource';
import {SecurityService} from '@app/service/security.service';
import {DatePipe} from '@angular/common';
import {ContactEmailResource} from '@app/resource/contact-email-resource';
import {ContactSiteResource} from '@app/resource/contact-site-resource';
import {CountryRepository} from '@app/repository/country/country.repository';
import {ContactSite, SiteType} from '@app/entity/contact-site';
import {ContactEmail, EmailType} from '@app/entity/contact-email';
import {ErrorService} from '@app/service/error.service';
import {PhoneNumberResource} from '@app/resource/phone-number-resource';
import {MessengerResource} from '@app/resource/messenger-resource';
import {RemoteRepository} from '@app/repository/remote.repository';

@Injectable({
    providedIn: 'root'
})
export class ContactRepository extends RemoteRepository<Contact, ContactResource> {

    constructor(
        private readonly countryRepository: CountryRepository,
        private readonly datePipe: DatePipe,
        securityService: SecurityService,
        errorService: ErrorService
    ) {
        super(securityService, errorService)
    }

    create(contact: Contact): Promise<Contact> {

        const body = this.convertToResource(contact);
        const url = `${environment.clientUrl}/contact`;

        return this.securityService.postRequest<ContactResource>(url, body)
            .then(response => this.convertFromResource(response.body))
            .catch(reason => this.errorService.convertError(reason));
    }

    update(contact: Contact): Promise<Contact> {

        const body = this.convertToResource(contact);
        const url = `${environment.clientUrl}/contact`;

        return this.securityService.putRequest<ContactResource>(url, body)
            .then(response => this.convertFromResource(response.body))
            .catch(reason => this.errorService.convertError(reason));
    }

    protected buildFetchUrl(): string {
        return `${environment.clientUrl}/contacts`;
    }

    protected buildFetchUrlById(id: number): string {
        return `${environment.clientUrl}/contact/${id}`;
    }

    protected buildEntityPageFetchUrl(pageSize: number, offset: number): string {
        return `${environment.clientUrl}/contacts/page?pageSize=${pageSize}&offset=${offset}`;
    }

    private convertToResource(contact: Contact): ContactResource {

        let body = new ContactResource();
        body.assigner = contact.assigner;
        body.curators = contact.curators;
        body.firstName = contact.firstName;
        body.secondName = contact.secondName;
        body.thirdName = contact.thirdName;
        body.company = contact.company;
        body.birthday = this.datePipe.transform(contact.birthday, 'yyyy-MM-dd');
        body.phoneNumbers = contact.phoneNumbers.map(value => PhoneNumberResource.of(value));
        body.emails = contact.emails.map(value => ContactEmailResource.valueOf(value));
        body.sites = contact.sites.map(value => ContactSiteResource.valueOf(value));
        body.messengers = contact.messengers.map(value => MessengerResource.of(value));

        if (contact.id && contact.id > 0) {
            body.id = contact.id;
        }

        if (contact.version) {
            body.version = contact.version;
        }

        return body;
    }

    protected convertFromResource(resource: ContactResource): Contact {

        const contact = Contact.create();
        contact.id = resource.id;
        contact.assigner = resource.assigner;
        contact.version = resource.version;
        contact.curators = resource.curators;
        contact.firstName = resource.firstName;
        contact.secondName = resource.secondName;
        contact.thirdName = resource.thirdName;
        contact.company = resource.company;
        contact.created = new Date(resource.created);
        contact.modified = new Date(resource.modified);
        contact.birthday = resource.birthday ? new Date(resource.birthday) : null;
        contact.sites = resource.sites
            .map(value => new ContactSite(value.url, value.type as SiteType));
        contact.messengers = resource.messengers
            .map(value => MessengerResource.toMessenger(value));
        contact.phoneNumbers = resource.phoneNumbers
            .map(value => PhoneNumberResource.toPhoneNumber(value));
        contact.emails = resource.emails
            .map(value => new ContactEmail(value.email, value.type as EmailType));

        return contact;
    }
}

