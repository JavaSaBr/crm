import {Entity} from '@app/entity/entity';

export enum SiteType {
    WORK = 'work',
    HOME = 'home'
}

export class ContactSite extends Entity {
    constructor(public url: string | null, public type: SiteType | null) {
        super();
    }
}
