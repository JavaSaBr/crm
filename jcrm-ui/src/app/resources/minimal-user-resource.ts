import {MinimalUser} from '@app/entity/minimal-user';

export class MinimalUserResource {

    constructor(
        public id: number | null,
        public email: string | null,
        public firstName: string | null,
        public secondName: string | null,
        public thirdName: string | null,
        public birthday: string | null,
    ) {
    }

    public toMinimalUser(): MinimalUser {
        return new MinimalUser(
            this.id,
            this.email,
            this.firstName,
            this.secondName,
            this.thirdName,
            new Date(this.birthday)
        );
    }
}
