import {Component, ElementRef} from '@angular/core';
import {MatFormFieldControl, MatSelectChange} from '@angular/material';
import {FormControl, NgControl, Validators} from '@angular/forms';
import {FocusMonitor} from '@angular/cdk/a11y';
import {MultiFieldsMultiEntityInput} from '@app/input/multi-fields-multi-entity-input';
import {ContactMessenger, MessengerType} from '@app/entity/contact-messenger';

@Component({
    selector: 'contact-messengers-input',
    templateUrl: './contact-messengers-input.html',
    styleUrls: ['./contact-messengers-input.scss'],
    providers: [{provide: MatFormFieldControl, useExisting: ContactMessengersInput}],
    host: {
        '[id]': 'id',
        '[attr.aria-describedby]': 'describedBy',
        '(focusout)': 'onTouched()',
    }
})
export class ContactMessengersInput extends MultiFieldsMultiEntityInput<ContactMessenger> {

    readonly availableEmailTypes: MessengerType[] = [
        MessengerType.HOME,
        MessengerType.WORK
    ];

    constructor(
        ngControl: NgControl,
        focusMonitor: FocusMonitor,
        elementRef: ElementRef<HTMLElement>
    ) {
        super(ngControl, focusMonitor, elementRef);
    }

    get controlType(): string {
        return 'contact-messengers-input';
    }

    protected createFormControls(entity: ContactMessenger): FormControl[] {
        return [
            new FormControl(entity.login, {
                validators: [Validators.required]
            }),
            new FormControl(entity.type, {
                validators: [Validators.required]
            })
        ];
    }

    addNew() {
        this.addEntity(new ContactMessenger('', MessengerType.WORK));
    }

    changeEmailType(contactEmail: ContactMessenger, event: MatSelectChange) {
        contactEmail.type = event.value as MessengerType;
    }
}
