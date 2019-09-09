import {UniqEntity} from '@app/entity/uniq-entity';

export class Contact extends UniqEntity {

    assigner: number | null;
    curators: number[] | null;

    firstName: string | null;
    secondName: string | null;
    thirdName: string | null;
    birthday: Date | null;

    constructor(contact?: Contact | null) {
        super(contact ? contact.id : 0);
        this.assigner = contact ? contact.assigner : null;
        this.curators = contact ? contact.curators : null;
        this.firstName = contact ? contact.firstName : null;
        this.secondName = contact ? contact.secondName : null;
        this.thirdName = contact ? contact.thirdName : null;
        this.birthday = contact ? contact.birthday : null;
    }
}
