import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {environment} from '../../../environments/environment';
import {OrganizationValidator} from '../../utils/validator/organization-validator';
import {TranslateService} from '@ngx-translate/core';
import {RegistrationService} from '../../services/registration.service';
import {ErrorService} from '../../services/error.service';
import {ErrorResponse} from '../../error/error-response';
import {AuthenticationInResource} from '../../resources/authentication-in-resource';
import {UserService} from '../../services/user.service';
import {Router} from '@angular/router';

@Component({
    selector: 'app-authentication',
    templateUrl: './authentication.component.html',
    styleUrls: ['./authentication.component.css'],
    host: {'class': 'full-screen-form'}
})
export class AuthenticationComponent {

    readonly authFormGroup: FormGroup;
    readonly login: FormControl;
    readonly password: FormControl;

    disabled: boolean;

    constructor(
        formBuilder: FormBuilder,
        private readonly translateService: TranslateService,
        private readonly registrationService: RegistrationService,
        private readonly errorService: ErrorService,
        private readonly userService: UserService,
        private readonly router: Router
    ) {
        this.authFormGroup = formBuilder.group({
            login: ['', [
                Validators.required,
                Validators.minLength(Math.min(environment.emailMinLength, environment.phoneNumberMinLength)),
                Validators.maxLength(Math.max(environment.emailMaxLength, environment.phoneNumberMaxLength))
            ]],
            password: ['', [
                Validators.required,
                Validators.minLength(environment.passwordMinLength),
                Validators.maxLength(environment.passwordMaxLength)
            ]],
        });

        this.login = this.authFormGroup.controls['login'] as FormControl;
        this.password = this.authFormGroup.controls['password'] as FormControl;
    }

    authenticate() {
        this.disabled = true;
        this.registrationService.authenticate(this.login.value as string, this.password.value as string)
            .then(value => this.finishAuthentication(value))
            .catch(reason => this.handleError(reason));
    }

    private handleError(reason: any) {
        let error = reason as ErrorResponse;
        this.errorService.showError(error.errorMessage);
        this.disabled = false;
    }

    private finishAuthentication(value: AuthenticationInResource) {
        this.userService.authenticate(value.user, value.token);
        this.disabled = false;
        this.router.navigate(['/']);
    }

    getPasswordErrorMessage(): string | null {

        if (this.password.hasError('required')) {
            return this.translateService.instant('FORMS.ERROR.PASSWORD.REQUIRED');
        } else if (this.password.hasError('minlength')) {
            return this.translateService.instant('FORMS.ERROR.PASSWORD.TOO_SHORT');
        } else if (this.password.hasError('maxlength')) {
            return this.translateService.instant('FORMS.ERROR.PASSWORD.TOO_LONG');
        }

        return null;
    }

    getLoginErrorMessage(): string | null {

        if (this.login.hasError('required')) {
            return this.translateService.instant('FORMS.ERROR.LOGIN.REQUIRED');
        } else if (this.login.hasError('minlength')) {
            return this.translateService.instant('FORMS.ERROR.LOGIN.TOO_SHORT');
        } else if (this.login.hasError('maxlength')) {
            return this.translateService.instant('FORMS.ERROR.LOGIN.TOO_LONG');
        }

        return null;
    }
}
