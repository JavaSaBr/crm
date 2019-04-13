import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {CountryRepository} from '../../repositories/country/country.repository';
import {NoAuthHomeService} from '../../services/no-auth-home.service';
import {PhoneNumberValidator} from '../../input/phone-number/phone-number-validator';
import {PasswordValidator} from '../../utils/validator/password-validator';
import {RegistrationService} from '../../services/registration.service';
import {CountryValidator} from '../../input/country/country-validator';
import {Country} from '../../entity/country';
import {PhoneNumber} from '../../input/phone-number/phone-number';
import {OrganizationValidator} from '../../utils/validator/organization-validator';
import {OtherUserNameValidator} from "../../utils/validator/other-user-name-validator";
import {UserValidator} from "../../utils/validator/user-validator";
import {ErrorService} from "../../services/error.service";

@Component({
    selector: 'app-register-new-organization',
    templateUrl: './register-new-organization.component.html',
    styleUrls: ['./register-new-organization.component.scss']
})
export class RegisterNewOrganizationComponent {

    public readonly orgFormGroup: FormGroup;
    public readonly ownerFormGroup: FormGroup;
    public readonly subscribeFormGroup: FormGroup;
    public readonly activationFormGroup: FormGroup;

    public orgName: FormControl;
    public country: FormControl;
    public email: FormControl;
    public phoneNumber: FormControl;

    public selectedEmail: string;
    public disabled: boolean;

    constructor(
        formBuilder: FormBuilder,
        private readonly countryRepository: CountryRepository,
        private readonly noAuthHomeService: NoAuthHomeService,
        private readonly registrationService: RegistrationService,
        private readonly errorService: ErrorService
    ) {
        this.orgFormGroup = formBuilder.group({
            orgName: ['', [
                Validators.required
            ], [
                new OrganizationValidator(registrationService)
            ]],
            country: ['', [
                Validators.required,
                CountryValidator.instance
            ]]
        });
        this.ownerFormGroup = formBuilder.group({
            firstName: ['', [
                OtherUserNameValidator.instance
            ]],
            secondName: ['', [
                OtherUserNameValidator.instance
            ]],
            email: ['', [
                Validators.required,
                Validators.pattern('^[_A-Za-z0-9-]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$')
            ], [
                new UserValidator(registrationService)
            ]],
            password: ['', [
                Validators.required,
                new PasswordValidator(() => this.ownerFormGroup, 'password')
            ]],
            passwordConfirm: ['', [
                Validators.required,
                new PasswordValidator(() => this.ownerFormGroup, 'passwordConfirm')
            ]],
            phoneNumber: ['', [
                Validators.required,
                PhoneNumberValidator.instance,
            ]]
        });
        this.subscribeFormGroup = formBuilder.group({
            subscribe: ['false'],
        });
        this.activationFormGroup = formBuilder.group({
            activationCode: ['', Validators.required],
        });

        this.ownerFormGroup.controls['email'].valueChanges
            .subscribe(value => this.selectedEmail = value);

        this.disabled = false;

        this.orgName = this.orgFormGroup.controls['orgName'] as FormControl;
        this.country = this.orgFormGroup.controls['country'] as FormControl;
        this.email = this.ownerFormGroup.controls['email'] as FormControl;
        this.phoneNumber = this.ownerFormGroup.controls['phoneNumber'] as FormControl;
    }

    resetAndClose() {
        this.selectedEmail = null;
        this.orgFormGroup.reset();
        this.ownerFormGroup.reset();
        this.subscribeFormGroup.reset();
        this.noAuthHomeService.activateSubPage(null);
    }

    activateAndClose() {
        this.disabled = true;

        const orgName = this.orgName.value as string;
        const country = this.country.value as Country;

        let controls = this.ownerFormGroup.controls;

        const firstName = controls['firstName'].value as string;
        const secondName = controls['secondName'].value as string;
        const password = controls['password'].value as string;
        const phoneNumber = this.phoneNumber.value as PhoneNumber;
        const email = this.email.value as string;

        controls = this.subscribeFormGroup.controls;

        const subscribe = controls['subscribe'].value as boolean;

        controls = this.activationFormGroup.controls;

        const activationCode = controls['activationCode'].value as string;

        this.registrationService.register(
                orgName,
                country,
                firstName,
                secondName,
                email,
                activationCode,
                password,
                phoneNumber,
                subscribe
            )
            .then(value => {
                if (value != null) {
                    this.errorService.showError(value.errorMessage);
                } else {
                    this.resetAndClose();
                }
                this.disabled = false;
            })
    }

    sendEmailConfirmation() {
        this.disabled = true;

        const email = this.email.value as string;

        this.registrationService.confirmEmail(email)
            .then(value => {
                if (value != null) {
                    this.errorService.showError(value.errorMessage);
                } else {
                    this.resetAndClose();
                }
                this.disabled = false;
            })
    }

    getOrgNameErrorMessage() {

        if (this.orgName.hasError('required')) {
            return 'You must enter an organization name'
        } else if (OrganizationValidator.isTooShort(this.orgName)) {
            return 'Organization name is too short'
        } else if (OrganizationValidator.isTooLong(this.orgName)) {
            return 'Organization name is too long'
        } else if (OrganizationValidator.isAlreadyExist(this.orgName)) {
            return 'Organization is already exist'
        }

        return null
    }

    getCountryErrorMessage() {
        return this.country.hasError('required') ? 'You must select a country' : null;
    }

    getEmailErrorMessage() {

        if (this.email.hasError('required')) {
            return 'You must enter an email'
        } if (this.email.hasError('pattern')) {
            return 'Email is invalid'
        } else if (UserValidator.isTooShort(this.email)) {
            return 'Email is too short'
        } else if (UserValidator.isTooLong(this.email)) {
            return 'Email is too long'
        } else if (UserValidator.isAlreadyExist(this.email)) {
            return 'Email is already exist'
        }

        return null
    }

    getPhoneNumberErrorMessage() {

        if (this.phoneNumber.hasError('required')) {
            return 'You must enter a phone number'
        } else if (PhoneNumberValidator.isTooShort(this.phoneNumber)) {
            return 'Phone number is too short'
        } else if (PhoneNumberValidator.isTooLong(this.phoneNumber)) {
            return 'Phone number is too long'
        } else if (PhoneNumberValidator.isNoCountry(this.phoneNumber)) {
            return 'You must select a country code'
        } else if (PhoneNumberValidator.isInvalidPhoneNumber(this.phoneNumber)) {
            return 'Phone number is invalid'
        }

        return null
    }
}
