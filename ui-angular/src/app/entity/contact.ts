import {UniqEntity} from '@app/entity/uniq-entity';
import {ContactEmail} from '@app/entity/contact-email';
import {ContactSite} from '@app/entity/contact-site';
import {PhoneNumber} from '@app/entity/phone-number';
import {Messenger} from '@app/entity/messenger';

export class Contact extends UniqEntity {

    public static create(): Contact {
        return new Contact(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
        );
    }

    public static copy(another?: Contact | null): Contact {
        return new Contact(
            another ? another.id : 0,
            another ? another.assigner : null,
            another ? another.curators : null,
            another ? another.firstName : null,
            another ? another.secondName : null,
            another ? another.thirdName : null,
            another ? another.birthday : null,
            another ? another.created : null,
            another ? another.modified : null,
            another ? another.phoneNumbers : null,
            another ? another.emails : null,
            another ? another.sites : null,
            another ? another.messengers : null,
            another ? another.company : null,
            another ? another.version : null,
        );
    }

    private _namePresentation: string = null;

    constructor(
        id: number | null,
        public assigner: number | null,
        public curators: number[] | null,
        public firstName: string | null,
        public secondName: string | null,
        public thirdName: string | null,
        public birthday: Date | null,
        public created: Date | null,
        public modified: Date | null,
        public phoneNumbers: PhoneNumber[] | null,
        public emails: ContactEmail[] | null,
        public sites: ContactSite[] | null,
        public messengers: Messenger[] | null,
        public company: string | null,
        public version: number | null
    ) {
        super(id);
    }

    get namePresentation(): string {

        if (this._namePresentation == null) {
            this._namePresentation = this.buildNamePresentation();
        }

        return this._namePresentation;
    }

    private buildNamePresentation(): string {

        const names: string[] = [];

        if (this.firstName) {
            names.push(this.firstName);
        }

        if (this.secondName) {
            names.push(this.secondName);
        }

        if (this.thirdName) {
            names.push(this.thirdName);
        }

        return names.join(' ');
    }
}
