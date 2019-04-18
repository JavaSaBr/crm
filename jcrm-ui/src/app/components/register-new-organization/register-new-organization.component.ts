import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
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
import {TranslateService} from '@ngx-translate/core';
import {ErrorResponse} from '../../error/error-response';
import {UserService} from '../../services/user.service';
import {CountryRepository} from '../../repositories/country/country.repository';

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

    public selectedEmail;
    public disabled: boolean;
    public canEditSteps: boolean;

    constructor(
        formBuilder: FormBuilder,
        private readonly countryRepository: CountryRepository,
        private readonly noAuthHomeService: NoAuthHomeService,
        private readonly registrationService: RegistrationService,
        private readonly errorService: ErrorService,
        private readonly translateService: TranslateService,
        private readonly userService: UserService
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
                Validators.pattern(UserValidator.EMAIL_PATTERN)
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
            .subscribe(value => this.selectedEmail = {value: value});

        this.disabled = false;
        this.selectedEmail = {value: ''};

        this.orgName = this.orgFormGroup.controls['orgName'] as FormControl;
        this.country = this.orgFormGroup.controls['country'] as FormControl;
        this.email = this.ownerFormGroup.controls['email'] as FormControl;
        this.phoneNumber = this.ownerFormGroup.controls['phoneNumber'] as FormControl;
        this.canEditSteps = true;
    }

    resetAndClose() {
        this.selectedEmail = {value: ''};
        this.canEditSteps = true;
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
                this.userService.authenticate(value.user, value.token);
                this.disabled = false;
                this.resetAndClose();
            })
            .catch(reason => {
                let error = reason as ErrorResponse;
                this.errorService.showError(error.errorMessage);
                this.disabled = false;
            })
    }

    sendEmailConfirmation() {
        this.disabled = true;
        this.canEditSteps = false;

        const email = this.email.value as string;

        this.registrationService.confirmEmail(email)
            .then(value => {
                if (value != null) {
                    this.errorService.showError(value.errorMessage);
                }
                this.disabled = false;
            })
    }

    getOrgNameErrorMessage() {
        return OrganizationValidator.getNameErrorDescription(this.orgName, this.translateService);
    }

    getCountryErrorMessage() {
        return CountryValidator.getErrorDescription(this.country, this.translateService);
    }

    getEmailErrorMessage() {
        return UserValidator.getEmailErrorDescription(this.email, this.translateService);
    }

    getPhoneNumberErrorMessage() {
        return PhoneNumberValidator.getErrorDescription(this.phoneNumber, this.translateService);
    }
}
