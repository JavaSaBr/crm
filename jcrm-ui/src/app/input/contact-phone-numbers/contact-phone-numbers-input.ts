import {Component, ElementRef} from '@angular/core';
import {MatFormFieldControl, MatSelectChange} from '@angular/material';
import {FormControl, NgControl, Validators} from '@angular/forms';
import {FocusMonitor} from '@angular/cdk/a11y';
import {MultiFieldsMultiEntityInput} from '@app/input/multi-fields-multi-entity-input';
import {ContactPhoneNumber, PhoneNumberType} from '@app/entity/contact-phone-number';
import {PhoneNumberValidator} from '../phone-number/phone-number-validator';

@Component({
    selector: 'contact-phones-input',
    templateUrl: './contact-phone-numbers-input.html',
    styleUrls: ['./contact-phone-numbers-input.scss'],
    providers: [{provide: MatFormFieldControl, useExisting: ContactPhoneNumbersInput}],
    host: {
        '[id]': 'id',
        '[attr.aria-describedby]': 'describedBy',
        '(focusout)': 'onTouched()',
    }
})
export class ContactPhoneNumbersInput extends MultiFieldsMultiEntityInput<ContactPhoneNumber> {

    readonly availablePhoneTypes: PhoneNumberType[] = [
        PhoneNumberType.MOBILE,
        PhoneNumberType.WORK
    ];

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

    protected createFormControls(entity: ContactPhoneNumber): FormControl[] {
        return [
            new FormControl(entity.phoneNumber, {
                validators: [
                    Validators.required,
                    PhoneNumberValidator.FUN
                ]
            }),
            new FormControl(entity.type, {
                validators: [Validators.required]
            })
        ];
    }

    addNew() {
        this.addEntity(new ContactPhoneNumber('123456', PhoneNumberType.MOBILE));
    }

    changePhoneType(phoneNumber: ContactPhoneNumber, event: MatSelectChange) {
        phoneNumber.type = event.value as PhoneNumberType;
    }
}
