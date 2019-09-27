import {Component, ElementRef} from '@angular/core';
import {MatFormFieldControl} from '@angular/material';
import {User} from '@app/entity/user';
import {SingleEntityInput} from '@app/input/single-entity-input';
import {NgControl} from '@angular/forms';
import {FocusMonitor} from '@angular/cdk/a11y';
import {UserRepository} from '@app/repository/user/user.repository';
import {UserAutoCompleter} from '@app/util/auto-completer/user-auto-completer';
import {Observable} from 'rxjs';

@Component({
    selector: 'user-input',
    templateUrl: './user-input.html',
    styleUrls: ['./user-input.scss'],
    providers: [{provide: MatFormFieldControl, useExisting: UserInput}],
    host: {
        '[class.user-input-floating]': 'shouldLabelFloat',
        '[id]': 'id',
        '[attr.aria-describedby]': 'describedBy',
        '(focusout)': 'onTouched()',
    }
})
export class UserInput extends SingleEntityInput<User> {

    constructor(
        ngControl: NgControl,
        focusMonitor: FocusMonitor,
        elementRef: ElementRef<HTMLElement>,
        private readonly userRepository: UserRepository
    ) {
        super(ngControl, focusMonitor, elementRef);
    }

    get controlType(): string {
        return 'user-input';
    }

    protected displayWith(user: User | null): string {
        return user instanceof User ? user.namePresentation : '';
    }

    protected installAutoComplete(): Observable<User[]> {
        return UserAutoCompleter.install(this.entityControl, this.userRepository);
    }

    writeValue(value: any): void {

        if (value instanceof User) {
            this.value = value;
        } else {
            this.value = null;
        }

        super.writeValue(value);
    }
}
