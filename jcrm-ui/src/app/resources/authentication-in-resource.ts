import {User} from '@app/entity/user';

export class AuthenticationInResource {

    user: User | null;
    token: string | null;
}
