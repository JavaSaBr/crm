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

    public readonly orgFormGroup: FormGroup;
    public readonly ownerFormGroup: FormGroup;
    public readonly subscribeFormGroup: FormGroup;

    public selectedEmail: string;

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
        this.subscribeFormGroup = this.formBuilder.group({
            subscribe: ['false'],
        });

        this.ownerFormGroup.controls['email'].valueChanges
            .subscribe(value => this.selectedEmail = value);
    }

    ngOnInit() {
    }

    resetAndClose() {
        this.orgFormGroup.reset();
        this.noAuthHomeService.activateSubPage(null);
    }

    activateAndClose() {
        this.resetAndClose();
    }
}
