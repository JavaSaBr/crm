import {UniqEntity} from '@app/entity/uniq-entity';
import {ContactPhoneNumber} from '@app/entity/contact-phone-number';

export class Contact extends UniqEntity {

    public static create(contact?: Contact | null): Contact {

        return new Contact(
            contact ? contact.id : 0,
            contact ? contact.assigner : null,
            contact ? contact.curators : null,
            contact ? contact.firstName : null,
            contact ? contact.secondName : null,
            contact ? contact.thirdName : null,
            contact ? contact.birthday : null,
            contact ? contact.phoneNumbers : null,
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
        public phoneNumbers: ContactPhoneNumber[] | null
    ) {
        super(id);
    }
}
