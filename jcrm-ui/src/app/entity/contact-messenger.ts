import {Entity} from '@app/entity/entity';

export enum MessengerType {
    SKYPE = 'SKYPE',
    TELEGRAM = 'TELEGRAM',
    WHATS_UP = "WHATS_UP",
    VIBER = 'VIBER',
}

export class ContactMessenger extends Entity {
    constructor(public login: string | null, public type: MessengerType | null) {
        super();
    }
}
