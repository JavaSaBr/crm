import {MinimalUserResource} from '@app/resource/minimal-user-resource';
import {PhoneNumberResource} from '@app/resource/phone-number-resource';
import {User} from '@app/entity/user';
import {MessengerResource} from '@app/resource/messenger-resource';

export class UserResource extends MinimalUserResource {

    constructor(
        id: number | null,
        email: string | null,
        firstName: string | null,
        secondName: string | null,
        thirdName: string | null,
        birthday: Date | null,
        public phoneNumbers: PhoneNumberResource[] | null,
        public messengers: MessengerResource[] | null,
        public created: number | null,
        public modified: number | null,
    ) {
        super(id, email, firstName, secondName, thirdName, birthday);
    }

    public toUser(): User {
        return new User(
            this.id,
            this.email,
            this.firstName,
            this.secondName,
            this.thirdName,
            this.birthday,
            PhoneNumberResource.toPhoneNumbers(this.phoneNumbers),
            MessengerResource.toMessengers(this.messengers),
            this.created ? new Date(this.created) : null,
            this.modified ? new Date(this.modified) : null
        );
    }
}
