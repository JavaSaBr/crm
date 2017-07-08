import {Component} from "@angular/core";
import {UserCredentials} from "../../../model/user/user-credentials";
import {SecurityService} from "../../../service/security.service";
import {PageComponent} from "../../page.component";
import {Router} from "@angular/router";

@Component({
  moduleId: module.id,
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent extends PageComponent {

  /**
   * The current user info.
   */
  credentials: UserCredentials;

  /**
   * The error message.
   */
  error: string;

  constructor(private readonly security: SecurityService,
              private readonly router: Router) {
    super();
    this.credentials = new UserCredentials('', '');
    this.error = '';
  }

  /**
   * Try to auth using the current info.
   */
  tryAuth() {
    this.security.auth(this.credentials, (message, result) => {
      if (result) {
        this.error = '';
        this.credentials.username = '';
        this.credentials.password = '';
        this.router.navigateByUrl("/");
      } else {
        this.error = message;
      }
    });
  }
}
