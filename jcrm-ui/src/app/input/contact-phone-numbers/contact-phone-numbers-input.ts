import {Component, ElementRef} from '@angular/core';
import {MatFormFieldControl, MatSelectChange} from '@angular/material';
import {FormControl, NgControl, Validators} from '@angular/forms';
import {FocusMonitor} from '@angular/cdk/a11y';
import {MultiFieldsMultiEntityInput} from '@app/input/multi-fields-multi-entity-input';
import {ContactPhoneNumber, PhoneNumberType} from '@app/entity/contact-phone-number';
import {PhoneNumberValidator} from '../phone-number/phone-number-validator';
import {PhoneNumber} from '@app/entity/phone-number';
import {TranslateService} from '@ngx-translate/core';

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
        elementRef: ElementRef<HTMLElement>,
        translateService: TranslateService
    ) {
        super(ngControl, focusMonitor, elementRef, translateService);
    }

    get controlType(): string {
        return 'contact-phone-numbers-input';
    }

    protected createFormControls(phoneNumber: ContactPhoneNumber): FormControl[] {

        const phoneNumberControl = new FormControl(phoneNumber.phoneNumber, {
            validators: [
                Validators.required,
                PhoneNumberValidator.FUN
            ]
        });
        const typeControl = new FormControl(phoneNumber.type, {
            validators: [Validators.required]
        });

        phoneNumberControl.valueChanges
            .subscribe(value => phoneNumber.phoneNumber = value);
        typeControl.valueChanges
            .subscribe(value => phoneNumber.type = value);

        return [
            phoneNumberControl,
            typeControl
        ];
    }

    addNew(): void {
        this.addEntity(new ContactPhoneNumber(
            new PhoneNumber(null, '', ''),
            PhoneNumberType.WORK
        ));
    }

    changePhoneType(phoneNumber: ContactPhoneNumber, event: MatSelectChange): void {
        phoneNumber.type = event.value as PhoneNumberType;
    }

    getPhoneNumberTypeDescription(phoneNumberType: PhoneNumberType): void {
        return this.translateService.instant(`ENUM.PHONE_NUMBER_TYPE.${phoneNumberType}`);
    }
}
