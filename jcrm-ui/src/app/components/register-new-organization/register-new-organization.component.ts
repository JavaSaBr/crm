import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {PhoneNumberValidator} from '@app/input/phone-number/phone-number-validator';
import {PasswordValidator} from '@app/util/validator/password-validator';
import {RegistrationService} from '@app/service/registration.service';
import {CountryValidator} from '@app/input/country/country-validator';
import {Country} from '@app/entity/country';
import {PhoneNumber} from '@app/input/phone-number/phone-number';
import {OrganizationValidator} from '@app/util/validator/organization-validator';
import {OtherUserNameValidator} from '@app/util/validator/other-user-name-validator';
import {UserValidator} from '@app/util/validator/user-validator';
import {ErrorService} from '@app/service/error.service';
import {TranslateService} from '@ngx-translate/core';
import {ErrorResponse} from '@app/error/error-response';
import {CountryRepository} from '@app/repository/country/country.repository';
import {AuthenticationInResource} from '@app/resource/authentication-in-resource';
import {Router} from '@angular/router';
import {environment} from '@app/env/environment';
import {SecurityService} from '@app/service/security.service';
import {UiUtils} from '@app/util/ui-utils';
import {MatHorizontalStepper} from '@angular/material';

@Component({
    selector: 'app-register-new-organization',
    templateUrl: './register-new-organization.component.html',
    styleUrls: ['./register-new-organization.component.scss'],
    host: {'class': 'full-screen-form'}
})
export class RegisterNewOrganizationComponent implements OnInit {

    readonly rowHeight = UiUtils.FORM_ROW_HEIGHT;
    readonly rowRadioButtonHeight = UiUtils.FORM_ROW_RADIO_BUTTON_HEIGHT;
    readonly gutterSize = UiUtils.FORM_GUTTER_SIZE;

    readonly orgFormGroup: FormGroup;
    readonly ownerFormGroup: FormGroup;
    readonly subscribeFormGroup: FormGroup;
    readonly activationFormGroup: FormGroup;

    readonly orgName: FormControl;
    readonly country: FormControl;
    readonly email: FormControl;
    readonly phoneNumber: FormControl;

    @ViewChild(MatHorizontalStepper, {static: true})
    stepper: MatHorizontalStepper;

    @ViewChild('orgNameField', {static: true})
    orgNameField: ElementRef;

    @ViewChild('emailField', {static: true})
    emailField: ElementRef;

    @ViewChild('activationCodeField', {static: true})
    activationCodeField: ElementRef;

    selectedEmail = {value: ''};
    disabled: boolean;
    canEditSteps: boolean;

    constructor(
        formBuilder: FormBuilder,
        private readonly countryRepository: CountryRepository,
        private readonly registrationService: RegistrationService,
        private readonly errorService: ErrorService,
        private readonly translateService: TranslateService,
        private readonly securityService: SecurityService,
        private readonly router: Router
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
                Validators.minLength(environment.passwordMinLength),
                Validators.maxLength(environment.passwordMaxLength),
                new PasswordValidator(() => this.ownerFormGroup, 'password')
            ]],
            passwordConfirm: ['', [
                Validators.required,
                Validators.minLength(environment.passwordMinLength),
                Validators.maxLength(environment.passwordMaxLength),
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

    ngOnInit(): void {
        setTimeout(() => this.orgNameField.nativeElement.focus(), 100);
        this.stepper.selectionChange.subscribe(value => {
            setTimeout(() => {
                switch (value.selectedIndex) {
                    case 0: {
                        this.orgNameField.nativeElement.focus();
                        break;
                    }
                    case 1: {
                        this.emailField.nativeElement.focus();
                        break;
                    }
                    case 3: {
                        this.activationCodeField.nativeElement.focus();
                        break;
                    }
                }
            }, 100);
        });
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

        let result = this.registrationService.register(
            orgName,
            country,
            firstName,
            secondName,
            email,
            activationCode,
            password,
            phoneNumber,
            subscribe
        );

        result.then(value => this.finishRegistration(value))
            .catch(reason => this.handleError(reason));
    }

    private handleError(reason: any) {
        let error = reason as ErrorResponse;
        this.errorService.showError(error.errorMessage);
        this.disabled = false;
    }

    private finishRegistration(value: AuthenticationInResource) {
        this.securityService.authenticate(value.user, value.token);
        this.disabled = false;
        this.router.navigate(['/']);
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
            });
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
