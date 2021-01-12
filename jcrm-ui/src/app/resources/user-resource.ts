import {MinimalUserResource} from '@app/resource/minimal-user-resource';
import {PhoneNumberResource} from '@app/resource/phone-number-resource';
import {User} from '@app/entity/user';
import {MessengerResource} from '@app/resource/messenger-resource';

export class UserResource extends MinimalUserResource {

    public static toUser(resource: UserResource): User {
        return new User(
            resource.id,
            resource.email,
            resource.firstName,
            resource.secondName,
            resource.thirdName,
            resource.birthday ? new Date(resource.birthday) : null,
            PhoneNumberResource.toPhoneNumbers(resource.phoneNumbers),
            MessengerResource.toMessengers(resource.messengers),
            resource.password,
            resource.created ? new Date(resource.created) : null,
            resource.modified ? new Date(resource.modified) : null
        );
    }

    constructor(
        id: number | null,
        email: string | null,
        firstName: string | null,
        secondName: string | null,
        thirdName: string | null,
        birthday: string | null,
        public phoneNumbers: PhoneNumberResource[] | null,
        public messengers: MessengerResource[] | null,
        public password: string | null,
        public created: number | null,
        public modified: number | null,
    ) {
        super(id, email, firstName, secondName, thirdName, birthday);
    }
}
