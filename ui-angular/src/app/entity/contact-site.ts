import {Entity} from '@app/entity/entity';

export enum SiteType {
    WORK = 'WORK',
    HOME = 'HOME'
}

export class ContactSite extends Entity {
    constructor(public url: string | null, public type: SiteType | null) {
        super();
    }
}
