import {Component, ElementRef, Input, OnInit, Optional, Self} from '@angular/core';
import {Country} from '@app/entity/country';
import {AbstractControl, FormBuilder, FormGroup, NgControl} from '@angular/forms';
import {PhoneNumber} from '@app/entity/phone-number';
import {MatFormFieldControl} from '@angular/material';
import {Observable} from 'rxjs';
import {CountryRepository} from '@app/repository/country/country.repository';
import {BaseInput} from '../base-input';
import {FocusMonitor} from '@angular/cdk/a11y';
import {CountryPhoneCodeAutoCompleter} from '@app/util/auto-completer/country-phone-code-auto-completer';

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

    private _filteredCountries: Observable<Country[]>;
    private _selectedCountry: Country | string | null;

    constructor(
        formBuilder: FormBuilder,
        @Optional() @Self() ngControl: NgControl,
        focusMonitor: FocusMonitor,
        elementRef: ElementRef<HTMLElement>,
        private readonly countryRepository: CountryRepository
    ) {
        super(ngControl, focusMonitor, elementRef);

        this._filteredCountries = PhoneNumberInput.EMPTY_OBSERVABLE;
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

    get filteredCountries(): Observable<Country[]> {
        return this._filteredCountries;
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

        const country = this.selectedCountry;
        const phoneNumber = this.phoneNumberControl.value;

        if (country instanceof Country) {
            return new PhoneNumber(country, phoneNumber);
        } else {
            return new PhoneNumber(null, phoneNumber);
        }
    }

    set value(value: PhoneNumber | string | null) {

        if (value instanceof PhoneNumber) {
            this.countryControl.setValue(value.country);
            this.phoneNumberControl.setValue(value.phoneNumber);
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

        if (value instanceof Country) {
            this.selectedCountry = value;
            return;
        } else if (!(typeof value === 'string')) {
            return;
        }

        if (value.length < 1) {
            this.selectedCountry = null;
        } else {
            this.countryRepository.findByPhoneCode(value)
                .then(selectedCountry => {
                    if (selectedCountry == null) {
                        this.selectedCountry = value;
                    } else {
                        this.selectedCountry = selectedCountry;
                    }
                });
        }
    }

    public ngOnInit(): void {
        this._filteredCountries = CountryPhoneCodeAutoCompleter.install(this.countryControl, this.countryRepository);
    }

    displayCountry(country?: Country): string {
        return country ? country.phoneCode : '';
    }
}
