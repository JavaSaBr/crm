import {ContactEmailResource} from '@app/resource/contact-email-resource';
import {ContactSiteResource} from '@app/resource/contact-site-resource';
import {EntityResource} from '@app/resource/entity-resource';
import {PhoneNumberResource} from '@app/resource/phone-number-resource';
import {MessengerResource} from '@app/resource/messenger-resource';

export class ContactResource extends EntityResource {

    id: number;
    assigner: number;
    created: number;
    modified: number;
    curators: number[];
    version: number;

    firstName: string;
    secondName: string;
    thirdName: string;
    company: string;
    birthday: string;

    phoneNumbers: PhoneNumberResource[];
    emails: ContactEmailResource[];
    sites: ContactSiteResource[];
    messengers: MessengerResource[];
}
