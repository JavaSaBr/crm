import {Injectable} from '@angular/core';
import {environment} from '@app/env/environment';
import {ErrorResponse} from '@app/error/error-response';
import {Contact} from '@app/entity/contact';
import {CreateContactOutResource} from '@app/resource/create-contact-out-resource';
import {RemoteRepository} from '@app/repository/remote.repository';

@Injectable({
    providedIn: 'root'
})
export class ContactRepository extends RemoteRepository<Contact> {

    create(contact: Contact): Promise<Contact | null> {

        let body = new CreateContactOutResource(
            contact.firstName,
            contact.secondName,
            contact.thirdName
        );

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
        return new Contact(contact);
    }
}

