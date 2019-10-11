import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {environment} from '@app/env/environment';
import {TranslateService} from '@ngx-translate/core';
import {RegistrationService} from '@app/service/registration.service';
import {ErrorService} from '@app/service/error.service';
import {AuthenticationInResource} from '@app/resource/authentication-in-resource';
import {Router} from '@angular/router';
import {SecurityService} from '@app/service/security.service';

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
        private readonly translateService: TranslateService,
        private readonly registrationService: RegistrationService,
        private readonly errorService: ErrorService,
        private readonly securityService: SecurityService,
        private readonly router: Router,
        formBuilder: FormBuilder
    ) {
        this.disabled = false;
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

    authenticate(): void {
        this.disabled = true;
        this.registrationService.authenticate(this.login.value as string, this.password.value as string)
            .then(value => this.finishAuthentication(value))
            .catch(reason => this.errorService.showError(reason))
            .finally(() => this.disabled = false);
    }

    private finishAuthentication(value: AuthenticationInResource): void {
        this.securityService.authenticate(value.user, value.token);
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
