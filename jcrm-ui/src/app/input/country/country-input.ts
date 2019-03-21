import {BaseInput} from '../base-input';
import {Country} from '../../entity/country';
import {Component, ElementRef, Input, OnInit, Optional, Self} from '@angular/core';
import {MatFormFieldControl} from '@angular/material';
import {AbstractControl, FormBuilder, FormGroup, NgControl} from '@angular/forms';
import {FocusMonitor} from '@angular/cdk/a11y';
import {CountryRepository} from '../../repositories/country/country.repository';
import {Observable} from 'rxjs';
import {CountryAutocompleter} from '../../utils/country-autocompleter';

@Component({
    selector: 'country-input',
    templateUrl: './country-input.html',
    styleUrls: ['./country-input.scss'],
    providers: [{provide: MatFormFieldControl, useExisting: CountryInput}],
    host: {
        '[class.country-input-floating]': 'shouldLabelFloat',
        '[id]': 'id',
        '[attr.aria-describedby]': 'describedBy',
        '(focusout)': 'onTouched()',
    }
})
export class CountryInput extends BaseInput<Country> implements OnInit {

    public readonly formGroup: FormGroup;
    private readonly countryControl: AbstractControl;

    public filteredCountries: Observable<Country[]>;

    private _selectedCountry: Country | null;

    constructor(
        formBuilder: FormBuilder,
        @Optional() @Self() ngControl: NgControl,
        focusMonitor: FocusMonitor,
        elementRef: ElementRef<HTMLElement>,
        private countryRepository: CountryRepository
    ) {
        super(ngControl, focusMonitor, elementRef);

        this.formGroup = formBuilder.group({country: ''});

        this.countryControl = this.formGroup.controls['country'];
        this.countryControl.valueChanges
            .subscribe(value => this.extractCountry(value));
    }

    set selectedCountry(selectedCountry: Country | null) {
        this._selectedCountry = selectedCountry;
        this.changeFromSubControls();
    }

    get selectedCountry(): Country | null {
        return this._selectedCountry;
    }

    get controlType(): string {
        return 'country-input';
    }

    get empty(): boolean {
        return !this.countryControl.value;
    }

    @Input()
    get value(): Country | null {
        return this.selectedCountry;
    }

    set value(country: Country | null) {

        if (country != null) {
            this.countryControl.setValue(country);
        } else {
            this.countryControl.setValue(null);
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
            this.countryRepository.findByLowerCaseName(countryValue.toString().toLowerCase())
                .then(selectedCountry => this.selectedCountry = selectedCountry);
        }
    }

    public ngOnInit(): void {
        this.filteredCountries = new CountryAutocompleter(this.countryRepository, this.countryControl)
            .getFilteredCountries();
    }

    displayCountry(country?: Country): string {
        return country ? country.name : '';
    }
}