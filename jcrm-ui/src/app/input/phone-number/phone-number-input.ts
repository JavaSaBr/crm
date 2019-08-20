import {Component, ElementRef, Input, OnInit, Optional, Self} from '@angular/core';
import {Country} from '@app/entity/country';
import {AbstractControl, FormBuilder, FormGroup, NgControl} from '@angular/forms';
import {PhoneNumber} from './phone-number';
import {MatFormFieldControl} from '@angular/material';
import {Observable} from 'rxjs';
import {CountryRepository} from '@app/repository/country/country.repository';
import {BaseInput} from '../base-input';
import {FocusMonitor} from '@angular/cdk/a11y';
import {CountryPhoneCodeAutocompleter} from '@app/util/country-phone-code-autocompleter';

@Component({
    selector: 'phone-number-input',
    templateUrl: './phone-number-input.html',
    styleUrls: ['./phone-number-input.scss'],
    providers: [{provide: MatFormFieldControl, useExisting: PhoneNumberInput}],
    host: {
        '[class.phone-number-input-floating]': 'shouldLabelFloat',
        '[id]': 'id',
        '[attr.aria-describedby]': 'describedBy',
        '(focusout)': 'onTouched()',
    }
})
export class PhoneNumberInput extends BaseInput<PhoneNumber | string> implements OnInit {

    private static readonly EMPTY_OBSERVABLE = new Observable<Country[]>();

    public readonly formGroup: FormGroup;

    private readonly countryControl: AbstractControl;
    private readonly phoneNumberControl: AbstractControl;

    public filteredCountries: Observable<Country[]>;

    private _selectedCountry: Country | string | null;

    constructor(
        formBuilder: FormBuilder,
        @Optional() @Self() ngControl: NgControl,
        focusMonitor: FocusMonitor,
        elementRef: ElementRef<HTMLElement>,
        private countryRepository: CountryRepository
    ) {
        super(ngControl, focusMonitor, elementRef);

        this.filteredCountries = PhoneNumberInput.EMPTY_OBSERVABLE;
        this._selectedCountry = null;

        this.formGroup = formBuilder.group({
            country: '',
            phoneNumber: '',
        });

        this.countryControl = this.formGroup.controls['country'];
        this.countryControl.valueChanges
            .subscribe(value => this.extractCountry(value));

        this.phoneNumberControl = this.formGroup.controls['phoneNumber'];
        this.phoneNumberControl.valueChanges
            .subscribe(() => this.changeFromSubControls());
    }

    set selectedCountry(selectedCountry: Country | string | null) {
        this._selectedCountry = selectedCountry;
        this.changeFromSubControls();
    }

    get selectedCountry(): Country | string | null {
        return this._selectedCountry;
    }

    get controlType(): string {
        return 'phone-number-input';
    }

    get empty(): boolean {

        const country = this.selectedCountry;
        const phoneNumber = this.phoneNumberControl.value;

        return country == null && !phoneNumber;
    }

    @Input()
    get value(): PhoneNumber | string | null {

        const country = this.selectedCountry as Country;
        const phoneNumber = this.phoneNumberControl.value;

        if (country && country.id) {
            return new PhoneNumber(country, phoneNumber);
        } else {
            return new PhoneNumber(null, phoneNumber);
        }
    }

    set value(value: PhoneNumber | string | null) {

        let phoneNumber = value as PhoneNumber;
        if (phoneNumber && phoneNumber.country && phoneNumber.phoneNumber) {
            this.countryControl.setValue(phoneNumber.country);
            this.phoneNumberControl.setValue(phoneNumber.phoneNumber);
        } else {
            this.countryControl.setValue(null);
            this.phoneNumberControl.setValue('');
        }

        this.stateChanges.next();
    }

    private changeFromSubControls() {
        this.stateChanges.next();
        this.onChange(this.value);
    }

    private extractCountry(value: any) {

        const countryValue = value as Country;

        if (countryValue && countryValue.id) {
            this.countryRepository.findById(countryValue.id)
                .then(selectedCountry => this.selectedCountry = selectedCountry);
        } else {
            this.countryRepository.findByPhoneCode(countryValue.toString())
                .then(selectedCountry => {
                    if (selectedCountry == null) {
                        this.selectedCountry = countryValue
                    } else {
                        this.selectedCountry = selectedCountry
                    }
                });
        }
    }

    public ngOnInit(): void {
        this.filteredCountries = new CountryPhoneCodeAutocompleter(this.countryRepository, this.countryControl)
            .filteredCountries;
    }

    displayCountry(country?: Country): string {
        return country ? country.phoneCode : '';
    }
}
