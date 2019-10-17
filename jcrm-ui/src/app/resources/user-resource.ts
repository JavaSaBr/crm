import {MinimalUserResource} from '@app/resource/minimal-user-resource';

export class UserResource extends MinimalUserResource {

    constructor(
        id: number | null,
        email: string | null,
        firstName: string | null,
        secondName: string | null,
        thirdName: string | null,
        phoneNumber: string | null,
        public created: number | null,
        public modified: number | null,
    ) {
        super(id, email, firstName, secondName, thirdName, phoneNumber);
    }
}
