import {Entity} from '@app/entity/entity';

export enum EmailType {
    WORK = 'work',
    HOME = 'home'
}

export class ContactEmail extends Entity {
    constructor(public email: string | null, public type: EmailType | null) {
        super();
    }
}
