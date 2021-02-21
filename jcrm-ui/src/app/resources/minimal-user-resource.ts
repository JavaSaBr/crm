import {MinimalUser} from '@app/entity/minimal-user';

export class MinimalUserResource {

    public static toMinimalUser(resource: MinimalUserResource): MinimalUser {
        return new MinimalUser(
            resource.id,
            resource.email,
            resource.firstName,
            resource.secondName,
            resource.thirdName,
            new Date(resource.birthday)
        );
    }

    constructor(
        public id: number | null,
        public email: string | null,
        public firstName: string | null,
        public secondName: string | null,
        public thirdName: string | null,
        public birthday: string | null,
    ) {}
}
