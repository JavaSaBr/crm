import {ContactEmail} from '@app/entity/contact-email';

export class ContactEmailResource {

    public static valueOf(contactEmail: ContactEmail): ContactEmailResource {
        return new ContactEmailResource(contactEmail.email, contactEmail.type);
    }

    constructor(
        public email: string,
        public type: string
    ) {
    }
}
