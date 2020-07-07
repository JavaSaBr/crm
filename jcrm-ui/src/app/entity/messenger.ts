import {Entity} from '@app/entity/entity';

export enum MessengerType {
    UNKNOWN = 'UNKNOWN',
    SKYPE = 'SKYPE',
    TELEGRAM = 'TELEGRAM',
    WHATS_UP = "WHATS_UP",
    VIBER = 'VIBER',
}

export class Messenger extends Entity {

    private static readonly messengerTypeToId = new Map<MessengerType, number>([
        [MessengerType.UNKNOWN, 0],
        [MessengerType.SKYPE, 1],
        [MessengerType.TELEGRAM, 2],
        [MessengerType.WHATS_UP, 3],
        [MessengerType.VIBER, 4],
    ]);

    private static readonly idToMessengerType = new Map<number, MessengerType>([
        [0, MessengerType.UNKNOWN],
        [1, MessengerType.SKYPE],
        [2, MessengerType.TELEGRAM],
        [3, MessengerType.WHATS_UP],
        [4, MessengerType.VIBER],
    ]);

    public static getMessengerTypeId(messengerType: MessengerType): number {
        if (messengerType == null) {
            return 0;
        } else {
            return this.messengerTypeToId.get(messengerType);
        }
    }

    public static getMessengerTypeById(id: number): MessengerType | null {
        return this.idToMessengerType.get(id);
    }

    constructor(public login: string | null, public type: MessengerType | null) {
        super();
    }
}
