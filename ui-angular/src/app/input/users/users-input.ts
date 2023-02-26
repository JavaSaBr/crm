import {Component, ElementRef} from '@angular/core';
import {User} from '@app/entity/user';
import {NgControl} from '@angular/forms';
import {FocusMonitor} from '@angular/cdk/a11y';
import {UserRepository} from '@app/repository/user/user.repository';
import {Observable} from 'rxjs';
import {MultiEntityInput} from '@app/input/multi-entity-input';
import {UserAutoCompleter} from '@app/util/auto-completer/user-auto-completer';
import {MinimalUser} from '@app/entity/minimal-user';
import {MatFormFieldControl} from '@app/node-modules/@angular/material/form-field';

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
export class UsersInput extends MultiEntityInput<MinimalUser> {

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

    protected displayWith(user?: MinimalUser): string {
        return user instanceof MinimalUser ? user.namePresentation : '';
    }

    protected installAutoComplete(): Observable<MinimalUser[]> {
        return UserAutoCompleter.install(this.entityControl, this.userRepository);
    }

    protected inputToEntity(value: any): MinimalUser | null {
        if (value instanceof MinimalUser) {
            return value as MinimalUser;
        } else {
            return null;
        }
    }

    writeValue(value: any): void {

        if (value instanceof Array) {
            this.value = value;
        } else {
            this.value = [];
        }

        super.writeValue(value);
    }
}
