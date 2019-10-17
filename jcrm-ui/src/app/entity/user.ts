import {UserResource} from '@app/resource/user-resource';
import {MinimalUser} from '@app/entity/minimal-user';

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
            user ? user.phoneNumber : null,
            user ? user.created : null,
            user ? user.modified : null,
        );
    }

    public static from(resource: UserResource): User {
        return new User(
            resource.id,
            resource.email,
            resource.firstName,
            resource.secondName,
            resource.thirdName,
            resource.phoneNumber,
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
        phoneNumber: string | null,
        public created: Date | null,
        public modified: Date | null,
    ) {
        super(id, email, firstName, secondName, thirdName, phoneNumber);
    }
}
