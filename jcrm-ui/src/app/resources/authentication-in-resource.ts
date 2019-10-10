import {User} from '@app/entity/user';

export class AuthenticationInResource {

    public static of(another: AuthenticationInResource) {
        return new AuthenticationInResource(another.user, another.token);
    }

    constructor(
        public user: User | null,
        public token: string | null
    ) {
    }
}
