import {Component, ElementRef} from '@angular/core';
import {FormControl, NgControl, Validators} from '@angular/forms';
import {FocusMonitor} from '@angular/cdk/a11y';
import {MultiFieldsMultiEntityInput} from '@app/input/multi-fields-multi-entity-input';
import {PhoneNumberValidator} from '../phone-number/phone-number-validator';
import {PhoneNumber, PhoneNumberType} from '@app/entity/phone-number';
import {TranslateService} from '@ngx-translate/core';
import {CountryRepository} from '@app/repository/country/country.repository';
import {MatFormFieldControl} from '@app/node-modules/@angular/material/form-field';
import {MatSelectChange} from '@app/node-modules/@angular/material/select';

@Component({
    selector: 'phone-numbers-input',
    templateUrl: './phone-numbers-input.html',
    styleUrls: ['./phone-numbers-input.scss'],
    providers: [{provide: MatFormFieldControl, useExisting: PhoneNumbersInput}],
    host: {
        '[id]': 'id',
        '[attr.aria-describedby]': 'describedBy',
        '(focusout)': 'onTouched()',
    }
})
export class PhoneNumbersInput extends MultiFieldsMultiEntityInput<PhoneNumber> {

    readonly availablePhoneTypes: PhoneNumberType[] = [
        PhoneNumberType.MOBILE,
        PhoneNumberType.WORK
    ];

    constructor(
        private readonly countryRepository: CountryRepository,
        ngControl: NgControl,
        focusMonitor: FocusMonitor,
        elementRef: ElementRef<HTMLElement>,
        translateService: TranslateService
    ) {
        super(ngControl, focusMonitor, elementRef, translateService);
    }

    get controlType(): string {
        return 'phone-numbers-input';
    }

    protected createFormControls(phoneNumber: PhoneNumber): FormControl[] {

        const phoneNumberControl = new FormControl(phoneNumber.phoneNumber, {
            validators: [
                Validators.required,
                PhoneNumberValidator.fun
            ]
        });
        const typeControl = new FormControl(phoneNumber.type, {
            validators: [Validators.required]
        });

        phoneNumberControl.setValue(phoneNumber.phoneNumber);
        phoneNumberControl.valueChanges
            .subscribe(value => phoneNumber.phoneNumber = value);
        typeControl.setValue(phoneNumber.type);
        typeControl.valueChanges
            .subscribe(value => phoneNumber.type = value);

        return [
            phoneNumberControl,
            typeControl
        ];
    }

    addNew(): void {
        this.addEntity(new PhoneNumber(null, '', '', PhoneNumberType.WORK));
    }

    changePhoneType(phoneNumber: PhoneNumber, event: MatSelectChange): void {
        phoneNumber.type = event.value as PhoneNumberType;
    }

    getPhoneNumberTypeDescription(phoneNumberType: PhoneNumberType): void {
        return this.translateService.instant(`ENUM.PHONE_NUMBER_TYPE.${phoneNumberType}`);
    }
}
