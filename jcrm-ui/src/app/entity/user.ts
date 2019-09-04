import {Entity} from '@app/entity/entity';

export class User extends Entity {

    email: string | null;
    firstName: string | null;
    secondName: string | null;
    phoneNumber: string | null;

    private _namePresentation: string = null;

    constructor(user?: User) {
        super(user ? user.id : 0);
        this.email = user ? user.email : null;
        this.firstName = user ? user.firstName : null;
        this.secondName = user ? user.secondName : null;
        this.phoneNumber = user ? user.phoneNumber : null;
    }

    get namePresentation(): string {

        if (this._namePresentation == null) {
            this._namePresentation = this.prepareNamePresentation();
        }

        return this._namePresentation;
    }

    private prepareNamePresentation(): string {

        if (this.firstName || this.secondName) {

            let result = null;

            if (this.firstName) {
                result = this.firstName;
            }

            if (this.secondName) {
                if (result != null) {
                    result += ' ' + this.secondName;
                } else {
                    result = this.secondName;
                }
            }

            result += ` (${this.email})`;

            return result;
        }

        return this.email;
    }
}
