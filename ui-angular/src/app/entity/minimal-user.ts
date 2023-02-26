import {UniqEntity} from '@app/entity/uniq-entity';

export class MinimalUser extends UniqEntity {

    public static create(): MinimalUser {
        return new MinimalUser(
            null,
            null,
            null,
            null,
            null,
            null
        );
    }

    public static copy(user?: MinimalUser): MinimalUser {
        return new MinimalUser(
            user ? user.id : null,
            user ? user.email : null,
            user ? user.firstName : null,
            user ? user.secondName : null,
            user ? user.thirdName : null,
            user ? user.birthday : null,
        );
    }

    private _namePresentation: string = null;

    constructor(
        id: number | null,
        public email: string | null,
        public firstName: string | null,
        public secondName: string | null,
        public thirdName: string | null,
        public birthday: Date | null,
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

        if (this.thirdName) {
            names.push(this.thirdName);
        }

        return names.length > 0 ? names.join(' ') : this.email;
    }
}
