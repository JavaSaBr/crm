import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
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
/*            thirdName: ['', [
                OtherUserNameValidator.instance
            ]],*/
            email: ['', [
                Validators.required,
                Validators.email
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

        let controls = this.orgFormGroup.controls;

        const orgName = controls['orgName'].value as string;
        const country = controls['country'].value as Country;

        controls = this.ownerFormGroup.controls;

        const firstName = controls['firstName'].value as string;
        const secondName = controls['secondName'].value as string;
       // const thirdName = controls['thirdName'].value as string;
        const email = controls['email'].value as string;
        const password = controls['password'].value as string;
        const phoneNumber = controls['phoneNumber'].value as PhoneNumber;

        controls = this.subscribeFormGroup.controls;

        const subscribe = controls['subscribe'].value as boolean;

        controls = this.activationFormGroup.controls;

        const activationCode = controls['activationCode'].value as string;

        this.registrationService.register(
                orgName,
                country,
                firstName,
                secondName,
                null,
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

        const controls = this.ownerFormGroup.controls;
        const email = controls['email'].value as string;

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
}
