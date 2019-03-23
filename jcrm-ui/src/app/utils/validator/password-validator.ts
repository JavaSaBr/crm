import {AbstractControl, FormGroup, ValidationErrors, Validator} from '@angular/forms';

export class PasswordValidator implements Validator {

    constructor(
        private readonly formGroup: () => FormGroup | null,
        private readonly current: string,
        private readonly passwordName = 'password',
        private readonly passwordConfirmName = 'passwordConfirm',
    ) {
    }

    validate(control: AbstractControl): ValidationErrors | null {

        const formGroup = this.formGroup();

        if (!formGroup) {
            return null;
        }

        const passwordControl = formGroup.controls[this.passwordName];
        const passwordConfirmControl = formGroup.controls[this.passwordConfirmName];

        if (passwordControl.value === passwordConfirmControl.value) {

            setTimeout(() => {
                if (this.current === this.passwordName && !passwordConfirmControl.valid) {
                    passwordConfirmControl.setValue(passwordConfirmControl.value);
                } else if (this.current === this.passwordConfirmName && !passwordControl.valid) {
                    passwordControl.setValue(passwordControl.value);
                }
            }, 5);

            return null;

        } else {

            setTimeout(() => {
                if (this.current === this.passwordName && passwordConfirmControl.valid) {
                    passwordConfirmControl.setValue(passwordConfirmControl.value);
                } else if (this.current === this.passwordConfirmName && passwordControl.valid) {
                    passwordControl.setValue(passwordControl.value);
                }
            }, 5);

            return {'mismatch': true};
        }
    }
}