import {Component, ViewChild} from "@angular/core";
import {PageComponent} from "../../page.component";
import {RegisterUserCredentials} from "../../../user/register-user-credentials";
import {NgForm} from "@angular/forms";
import {EqualValidatorDirective} from "../../../validator/equal-validator";

@Component({
  moduleId: module.id,
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent extends PageComponent {

  /**
   * The current register user credentials.
   */
  credentials: RegisterUserCredentials;

  /**
   * The error message.
   */
  error: string;

  /**
   * The register form.
   */
  form: NgForm;

  /**
   * The current register form.
   */
  @ViewChild('registerForm')
  currentForm: NgForm;

  constructor() {
    super();
    this.credentials = new RegisterUserCredentials('', '', '');
    this.error = '';
  }

  /**
   * Try to auth using the current credentials.
   */
  tryRegister() {

  }

  ngAfterViewChecked() {
    this.formChanged();
  }

  formChanged() {

    if (this.currentForm === this.form) {
      return;
    }

    this.form = this.currentForm;

    if (this.form) {
      this.form.valueChanges
        .subscribe(data => this.onValueChanged(data));
    }
  }

  onValueChanged(data?: any) {

    if (!this.form) {
      return;
    }

    const form = this.form.form;

    for (const field in this.formErrors) {
      // clear previous error message (if any)
      this.formErrors[field] = '';
      const control = form.get(field);

      if (control && control.dirty && !control.valid) {
        const messages = this.validationMessages[field];
        for (const key in control.errors) {
          this.formErrors[field] += messages[key] + ' ';
        }
      }
    }
  }

  formErrors = {
    'name': '',
    'power': ''
  };

  validationMessages = {
    'name': {
      'required': 'Name is required.',
      'minlength': 'Name must be at least 4 characters long.',
      'maxlength': 'Name cannot be more than 24 characters long.',
      'forbiddenName': 'Someone named "Bob" cannot be a hero.'
    },
    'power': {
      'required': 'Power is required.'
    }
  };
}
