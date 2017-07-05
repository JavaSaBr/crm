import {Component, ViewChild} from "@angular/core";
import {PageComponent} from "../../page.component";
import {RegisterUserCredentials} from "../../../user/register-user-credentials";
import {NgForm} from "@angular/forms";
import {EqualsValidatorDirective} from "../../../validator/equal-validator";
import {SecurityService} from "../../../security.service";
import {Router} from "@angular/router";
import {LoginComponent} from "../login/login.component";

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

  constructor(private readonly security: SecurityService,
              private readonly router: Router) {
    super();
    this.credentials = new RegisterUserCredentials('', '', '');
    this.error = '';
  }

  /**
   * Try to register using the current credentials.
   */
  tryRegister() {
    this.security.register(this.credentials, (message, result) => {
      if (result) {
        this.error = '';
        this.credentials.username = '';
        this.credentials.password = '';
        this.router.navigateByUrl("/login");

      } else {
        this.error = message;
      }
    })
  }
}
