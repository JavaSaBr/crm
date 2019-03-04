import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Observable} from 'rxjs';
import {CountryRepository} from '../../repositories/country/country.repository';
import {NoAuthHomeService} from '../../services/no-auth-home.service';
import {CountryAutocompleter} from '../../utils/country-autocompleter';
import {Country} from '../../entity/country';

@Component({
    selector: 'app-register-new-organization',
    templateUrl: './register-new-organization.component.html',
    styleUrls: ['./register-new-organization.component.scss']
})
export class RegisterNewOrganizationComponent implements OnInit {

    filteredCountries: Observable<Country[]>;

    readonly orgFormGroup: FormGroup;
    readonly ownerFormGroup: FormGroup;

    constructor(
        private formBuilder: FormBuilder,
        private countryRepository: CountryRepository,
        private noAuthHomeService: NoAuthHomeService
    ) {
        this.orgFormGroup = this.formBuilder.group({
            orgName: ['', Validators.required],
            country: ['', Validators.required]
        });
        this.ownerFormGroup = this.formBuilder.group({
            firstName: ['', Validators.required],
            secondName: ['', Validators.required],
            thirdName: [''],
            email: ['', [
                Validators.required,
                Validators.email
            ]],
            phoneNumber: ['', [
                Validators.required,
                Validators.pattern('[0-9]{5}[-][0-9]{7}[-][0-9]{1}')
            ]]
        });
    }

    ngOnInit() {
        this.filteredCountries = new CountryAutocompleter(
                this.countryRepository,
                this.orgFormGroup.controls['country']
            )
            .getFilteredCountries();
    }

    resetAndClose() {
        this.orgFormGroup.reset();
        this.noAuthHomeService.activateSubPage(null);
    }
}
