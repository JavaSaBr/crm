import {Messenger} from '@app/entity/messenger';

export class MessengerResource {

    public static of(messenger: Messenger): MessengerResource {
        return new MessengerResource(
            messenger.login,
            Messenger.getMessengerTypeId(messenger.type)
        );
    }

    public static toMessenger(resource: MessengerResource): Messenger {
        return new Messenger(
            resource.login,
            Messenger.getMessengerTypeById(resource.type)
        );
    }

    constructor(
        public readonly login: string | null,
        public readonly type: number | null,
    ) {}
}
