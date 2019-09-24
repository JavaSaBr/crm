import {Entity} from '@app/entity/entity';

export enum EmailType {
    WORK = "WORK",
    HOME = "HOME"
}

export class ContactEmail extends Entity {
    constructor(public email: string | null, public type: EmailType | null) {
        super();
    }
}
