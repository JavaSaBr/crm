import {Component, ElementRef} from '@angular/core';
import {MatFormFieldControl} from '@angular/material';
import {User} from '@app/entity/user';
import {NgControl} from '@angular/forms';
import {FocusMonitor} from '@angular/cdk/a11y';
import {Observable, Subject} from 'rxjs';
import {MultiFieldsMultiEntityInput} from '@app/input/multi-fields-multi-entity-input';
import {ContactPhoneNumber} from '@app/entity/contact-phone-number';

@Component({
    selector: 'contact-phones-input',
    templateUrl: './contact-phone-numbers-input.html',
    styleUrls: ['./contact-phone-numbers-input.scss'],
    providers: [{provide: MatFormFieldControl, useExisting: ContactPhoneNumbersInput}],
    host: {
        '[class.users-input-floating]': 'shouldLabelFloat',
        '[id]': 'id',
        '[attr.aria-describedby]': 'describedBy',
        '(focusout)': 'onTouched()',
    }
})
export class ContactPhoneNumbersInput extends MultiFieldsMultiEntityInput<ContactPhoneNumber> {

    constructor(
        ngControl: NgControl,
        focusMonitor: FocusMonitor,
        elementRef: ElementRef<HTMLElement>
    ) {
        super(ngControl, focusMonitor, elementRef);
    }

    get controlType(): string {
        return 'contact-phone-numbers-input';
    }

    protected displayWith(user?: ContactPhoneNumber): string {
        return user instanceof User ? user.namePresentation : '';
    }

    protected installAutoComplete(): Observable<ContactPhoneNumber[]> {
        return new Subject();
    }

    protected inputToEntity(value: any): ContactPhoneNumber | null {
        return null;
    }

    addNew() {
        this.addEntity(new ContactPhoneNumber());
    }
}
