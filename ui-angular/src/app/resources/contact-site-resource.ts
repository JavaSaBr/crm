import {ContactSite} from '@app/entity/contact-site';

export class ContactSiteResource {

    public static valueOf(site: ContactSite): ContactSiteResource {
        return new ContactSiteResource(site.url, site.type);
    }

    constructor(
        public url: string,
        public type: string
    ) {
    }
}
