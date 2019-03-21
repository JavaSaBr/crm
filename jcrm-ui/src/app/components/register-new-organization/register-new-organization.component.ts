import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CountryRepository} from '../../repositories/country/country.repository';
import {NoAuthHomeService} from '../../services/no-auth-home.service';
import {PhoneNumberValidator} from '../../input/phone-number/phone-number-validator';

@Component({
    selector: 'app-register-new-organization',
    templateUrl: './register-new-organization.component.html',
    styleUrls: ['./register-new-organization.component.scss']
})
export class RegisterNewOrganizationComponent implements OnInit {

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
            firstName: [''],
            secondName: [''],
            thirdName: [''],
            email: ['', [
                Validators.required,
                Validators.email
            ]],
            phoneNumber: ['', [
                Validators.required,
                new PhoneNumberValidator()
            ]]
        });
    }

    ngOnInit() {
    }

    resetAndClose() {
        this.orgFormGroup.reset();
        this.noAuthHomeService.activateSubPage(null);
    }
}
