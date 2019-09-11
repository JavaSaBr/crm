import {Entity} from '@app/entity/entity';

export enum MessengerType {
    WORK = 'work',
    HOME = 'home'
}

export class ContactMessenger extends Entity {
    constructor(public name: string | null, public type: MessengerType | null) {
        super();
    }
}