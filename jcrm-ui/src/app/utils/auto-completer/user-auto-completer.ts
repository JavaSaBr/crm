import {AbstractControl} from '@angular/forms';
import {Observable} from 'rxjs';
import {User} from '@app/entity/user';
import {UserRepository} from '@app/repository/user/user.repository';
import {BaseAutoCompleter} from '@app/util/auto-completer/base-auto-completer';
import {MinimalUser} from '@app/entity/minimal-user';

export class UserAutoCompleter extends BaseAutoCompleter<MinimalUser> {

    public static install(control: AbstractControl, userRepository: UserRepository): Observable<MinimalUser[]> {
        return new UserAutoCompleter(userRepository, control)._filteredElements;
    }

    constructor(
        private readonly userRepository: UserRepository,
        control: AbstractControl,
    ) {
        super(control);
    }

    protected searchByString(value: string): void {
        this.userRepository.searchByName(value as string)
            .then(users => this._filteredElements.next(users))
            .catch(() => this._filteredElements.next([]));
    }
}
