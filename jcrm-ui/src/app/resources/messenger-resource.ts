import {Messenger} from '@app/entity/messenger';

export class MessengerResource {

    public static of(messenger: Messenger): MessengerResource {
        return new MessengerResource(
            messenger.login,
            Messenger.getMessengerTypeId(messenger.type)
        );
    }

    public static toMessengers(resources: MessengerResource[]): Messenger[] | null {
        if (resources == null || resources.length == 0) {
            return null;
        } else {
            return resources.map(value => value.toMessenger());
        }
    }

    constructor(
        public readonly login: string | null,
        public readonly type: number | null,
    ) {
    }

    public toMessenger(): Messenger {
        return new Messenger(
            this.login,
            Messenger.getMessengerTypeById(this.type)
        );
    }
}
