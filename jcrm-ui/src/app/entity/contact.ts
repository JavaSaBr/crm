import {UniqEntity} from '@app/entity/uniq-entity';
import {ContactPhoneNumber} from '@app/entity/contact-phone-number';
import {ContactEmail} from '@app/entity/contact-email';
import {ContactSite} from '@app/entity/contact-site';
import {ContactMessenger} from '@app/entity/contact-messenger';

export class Contact extends UniqEntity {

    public static create(
        contact?: Contact | null
    ): Contact {

        return new Contact(
            contact ? contact.id : 0,
            contact ? contact.assigner : null,
            contact ? contact.curators : null,
            contact ? contact.firstName : null,
            contact ? contact.secondName : null,
            contact ? contact.thirdName : null,
            contact ? contact.birthday : null,
            contact ? contact.created : null,
            contact ? contact.modified : null,
            contact ? contact.phoneNumbers : null,
            contact ? contact.emails : null,
            contact ? contact.sites : null,
            contact ? contact.messengers : null,
            contact ? contact.company : null,
            contact ? contact.version : null,
        );
    }

    constructor(
        id: number | null,
        public assigner: number | null,
        public curators: number[] | null,
        public firstName: string | null,
        public secondName: string | null,
        public thirdName: string | null,
        public birthday: Date | null,
        public created: Date | null,
        public modified: Date | number,
        public phoneNumbers: ContactPhoneNumber[] | null,
        public emails: ContactEmail[] | null,
        public sites: ContactSite[] | null,
        public messengers: ContactMessenger[] | null,
        public company: string | null,
        public version: number | null
    ) {
        super(id);
    }
}
