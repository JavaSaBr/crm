import {ContactPhoneNumberResource} from '@app/resource/contact-phone-number-resource';
import {ContactEmailResource} from '@app/resource/contact-email-resource';
import {ContactSiteResource} from '@app/resource/contact-site-resource';
import {ContactMessengerResource} from '@app/resource/contact-messenger-resource';
import {EntityResource} from '@app/resource/entity-resource';

export class ContactResource extends EntityResource {

    assigner: number;
    curators: number[];

    firstName: string;
    secondName: string;
    thirdName: string;
    company: string;
    birthday: string;

    phoneNumbers: ContactPhoneNumberResource[];
    emails: ContactEmailResource[];
    sites: ContactSiteResource[];
    messengers: ContactMessengerResource[];
}