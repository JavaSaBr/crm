import {Component, ElementRef} from '@angular/core';
import {MatFormFieldControl} from '@angular/material';
import {User} from '@app/entity/user';
import {NgControl} from '@angular/forms';
import {FocusMonitor} from '@angular/cdk/a11y';
import {UserRepository} from '@app/repository/user/user.repository';
import {Observable} from 'rxjs';
import {MultiEntityInput} from '@app/input/multi-entity-input';
import {UserAutoCompleter} from '@app/util/auto-completer/user-auto-completer';

@Component({
    selector: 'users-input',
    templateUrl: './users-input.html',
    styleUrls: ['./users-input.scss'],
    providers: [{provide: MatFormFieldControl, useExisting: UsersInput}],
    host: {
        '[class.users-input-floating]': 'shouldLabelFloat',
        '[id]': 'id',
        '[attr.aria-describedby]': 'describedBy',
        '(focusout)': 'onTouched()',
    }
})
export class UsersInput extends MultiEntityInput<User> {

    constructor(
        ngControl: NgControl,
        focusMonitor: FocusMonitor,
        elementRef: ElementRef<HTMLElement>,
        private readonly userRepository: UserRepository
    ) {
        super(ngControl, focusMonitor, elementRef);
    }

    get controlType(): string {
        return 'users-input';
    }

    protected displayWith(user?: User): string {
        return user instanceof User ? user.namePresentation : '';
    }

    protected installAutoComplete(): Observable<User[]> {
        return UserAutoCompleter.install(this.entityControl, this.userRepository);
    }

    protected inputToEntity(value: any): User | null {
        if (value instanceof User) {
            return value as User;
        } else {
            return null;
        }
    }
}
