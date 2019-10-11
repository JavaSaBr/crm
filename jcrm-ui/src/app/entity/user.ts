import {UniqEntity} from '@app/entity/uniq-entity';

export class User extends UniqEntity {

    public static create(): User {
        return new User(null, null, null, null, null);
    }

    public static copy(user?: User): User {
        return new User(
            user ? user.id : null,
            user ? user.email : null,
            user ? user.firstName : null,
            user ? user.secondName : null,
            user ? user.phoneNumber : null,
        );
    }

    private _namePresentation: string = null;

    constructor(
        id: number | null,
        public email: string | null,
        public firstName: string | null,
        public secondName: string | null,
        public phoneNumber: string | null,
    ) {
        super(id);
    }

    get namePresentation(): string {

        if (this._namePresentation == null) {
            this._namePresentation = this.buildNamePresentation();
        }

        return this._namePresentation;
    }

    private buildNamePresentation(): string {

        const names: string[] = [];

        if (this.firstName) {
            names.push(this.firstName);
        }

        if (this.secondName) {
            names.push(this.secondName);
        }

        return names.length > 0 ? names.join(' ') : this.email;
    }
}
