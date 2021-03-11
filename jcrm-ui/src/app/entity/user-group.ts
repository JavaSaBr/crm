import {UniqEntity} from '@app/entity/uniq-entity';

export class UserGroup extends UniqEntity {

    public static create(): UserGroup {
        return new UserGroup(
            null,
            null,
            null,
            null
        );
    }

    public static copy(userGroup?: UserGroup): UserGroup {
        return new UserGroup(
            userGroup ? userGroup.id : null,
            userGroup ? userGroup.name : null,
            userGroup ? userGroup.created : null,
            userGroup ? userGroup.modified : null
        );
    }

    constructor(
        id: number | null,
        public name: string | null,
        public created: Date | null,
        public modified: Date | null,
    ) {
        super(id);
    }
}
