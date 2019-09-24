import {ContactMessenger} from '@app/entity/contact-messenger';

export class ContactMessengerResource {

    public static valueOf(messenger: ContactMessenger): ContactMessengerResource {
        return new ContactMessengerResource(messenger.login, messenger.type);
    }

    constructor(
        private login: string,
        private type: string,
    ) {
    }
}
