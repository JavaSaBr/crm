import {AbstractControl} from '@angular/forms';
import {Observable} from 'rxjs';
import {User} from '@app/entity/user';
import {UserRepository} from '@app/repository/user/user.repository';
import {BaseAutoCompleter} from '@app/util/auto-completer/base-auto-completer';

export class UserAutoCompleter extends BaseAutoCompleter<User> {

    public static install(control: AbstractControl, userRepository: UserRepository): Observable<User[]> {
        return new UserAutoCompleter(control, userRepository)._filteredElements;
    }

    constructor(
        control: AbstractControl,
        private readonly userRepository: UserRepository,
    ) {
        super(control);
    }

    protected searchByString(value: string): void {
        this.userRepository.searchByName(value as string)
            .then(users => this._filteredElements.next(users));
    }
}
