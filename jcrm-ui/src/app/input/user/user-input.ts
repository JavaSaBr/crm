import {Component, ElementRef} from '@angular/core';
import {MatFormFieldControl} from '@angular/material';
import {User} from '@app/entity/user';
import {SingleEntityInput} from '@app/input/single-entity-input';
import {FormBuilder, NgControl} from '@angular/forms';
import {FocusMonitor} from '@angular/cdk/a11y';

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
        formBuilder: FormBuilder,
        ngControl: NgControl,
        focusMonitor: FocusMonitor,
        elementRef: ElementRef<HTMLElement>
    ) {
        super(formBuilder, ngControl, focusMonitor, elementRef);
    }

    get controlType(): string {
        return 'user-input';
    }

    protected displayWith(user?: User): string {
        return user && user.firstName ? user.firstName : '';
    }

    protected extractEntity(value: any): void {
    }
}
