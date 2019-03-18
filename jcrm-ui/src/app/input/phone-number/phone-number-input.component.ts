import {Component, ElementRef, forwardRef, Input, OnDestroy, OnInit, Optional, Self} from '@angular/core';
import {CountryRepository} from '../../repositories/country/country.repository';
import {Country} from '../../entity/country';
import {ControlValueAccessor, FormBuilder, FormGroup, NG_VALUE_ACCESSOR, NgControl} from '@angular/forms';
import {PhoneNumber} from './phone-number';
import {MatFormFieldControl} from '@angular/material';
import {Observable, Subject} from 'rxjs';
import {FocusMonitor} from '@angular/cdk/a11y';
import {coerceBooleanProperty} from '@angular/cdk/coercion';
import {CountryAutocompleter} from '../../utils/country-autocompleter';

@Component({
    selector: 'phone-number-input',
    templateUrl: './phone-number-input.component.html',
    styleUrls: ['./phone-number-input.component.scss'],
    providers: [
        {
            provide: MatFormFieldControl,
            useExisting: PhoneNumberInput
        },
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => PhoneNumberInput),
            multi: true
        }
    ],
    host: {
        '[class.phone-number-input-floating]': 'shouldLabelFloat',
        '[id]': 'id',
        '[attr.aria-describedby]': 'describedBy',
    }
})
export class PhoneNumberInput implements OnInit, MatFormFieldControl<PhoneNumber>, OnDestroy, ControlValueAccessor {

    static nextId = 0;

    id = `phone-number-input-${PhoneNumberInput.nextId++}`;
    controlType = 'phone-number-input';
    describedBy = '';

    errorState = false;

    parts: FormGroup;

    focused = false;
    autofilled = false;

    readonly stateChanges = new Subject<void>();

    filteredCountries: Observable<Country[]>;

    onChange = (value: any) => {};
    onTouched = () => {};

    private _placeholder: string;
    private _required = false;
    private _disabled = false;

    constructor(
        formBuilder: FormBuilder,
        @Optional() @Self() public ngControl: NgControl,
        private focusMonitor: FocusMonitor,
        private elementRef: ElementRef<HTMLElement>,
        private countryRepository: CountryRepository
    ) {

        this.parts = formBuilder.group({
            country: '',
            phoneNumber: '',
        });
        this.parts.valueChanges.subscribe(() => this.subControlChanged());

        focusMonitor.monitor(elementRef.nativeElement, true)
            .subscribe(origin => {
                this.focused = !!origin;
                this.stateChanges.next();
                if (!this.focused) {
                    this.onTouched();
                }
            });
    }

    private subControlChanged() {
        this.stateChanges.next();
        this.onChange(this.value);
    }

    ngOnInit() {
        this.countryRepository.findAll();
        this.filteredCountries = new CountryAutocompleter(this.countryRepository, this.parts.controls['country'])
            .getFilteredCountries();
    }

    @Input()
    get value(): PhoneNumber | null {

        const {value: {country, phoneNumber}} = this.parts;

        if (country && phoneNumber) {
            return new PhoneNumber(country, phoneNumber);
        } else {
            return null;
        }
    }

    set value(newValue: PhoneNumber | null) {
        const {country, phoneNumber} = newValue || new PhoneNumber(null, '');
        this.parts.setValue({country, phoneNumber});
        this.stateChanges.next();
    }

    @Input()
    get placeholder() {
        return this._placeholder;
    }

    set placeholder(placeholder: string) {
        this._placeholder = placeholder;
        this.stateChanges.next();
    }

    get empty() {
        const {value: {country, phoneNumber}} = this.parts;
        return !country && !phoneNumber;
    }

    get shouldLabelFloat() {
        return this.focused || !this.empty;
    }

    @Input()
    get required() {
        return this._required;
    }

    set required(required) {
        this._required = coerceBooleanProperty(required);
        this.stateChanges.next();
    }

    @Input()
    get disabled() {
        return this._disabled;
    }

    set disabled(disabled) {
        this._disabled = coerceBooleanProperty(disabled);
        this.stateChanges.next();
    }

    setDescribedByIds(ids: string[]) {
        this.describedBy = ids.join(' ');
    }

    onContainerClick(event: MouseEvent) {
        if ((event.target as Element).tagName.toLowerCase() != 'input') {
            this.elementRef.nativeElement.querySelector('input').focus();
        }
    }

    ngOnDestroy() {
        this.stateChanges.complete();
        this.focusMonitor.stopMonitoring(this.elementRef.nativeElement);
    }

    registerOnChange(fn: (value: any) => void): void {
        this.onChange = fn;
    }

    registerOnTouched(fn: () => void): void {
        this.onTouched = fn;
    }

    writeValue(value: any): void {
        this.onChange(this.value);
    }

    displayCountry(country?: Country): string | undefined {
        return country ? country.phoneCode : undefined;
    }
}
