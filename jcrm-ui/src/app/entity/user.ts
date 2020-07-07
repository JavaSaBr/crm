import {MinimalUser} from '@app/entity/minimal-user';
import {PhoneNumber} from '@app/entity/phone-number';
import {Messenger} from '@app/entity/messenger';

export class User extends MinimalUser {

    public static create(): User {
        return new User(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
    }

    public static copy(user?: User): User {
        return new User(
            user ? user.id : null,
            user ? user.email : null,
            user ? user.firstName : null,
            user ? user.secondName : null,
            user ? user.thirdName : null,
            user ? user.birthday : null,
            user ? user.phoneNumbers : null,
            user ? user.messengers : null,
            user ? user.created : null,
            user ? user.modified : null,
        );
    }

    constructor(
        id: number | null,
        email: string | null,
        firstName: string | null,
        secondName: string | null,
        thirdName: string | null,
        birthday: Date | null,
        public phoneNumbers: PhoneNumber[] | null,
        public messengers: Messenger[] | null,
        public created: Date | null,
        public modified: Date | null,
    ) {
        super(id, email, firstName, secondName, thirdName, birthday);
    }
}
