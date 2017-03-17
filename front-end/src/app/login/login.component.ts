import {Component, OnInit} from "@angular/core";
import {UserCredentials} from "./user-credentials";
import {SecurityService} from "../security.service";

@Component({
  moduleId: module.id,
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  /**
   * The current user credentials.
   */
  credentials: UserCredentials = new UserCredentials('', '');

  /**
   * The flag of submiting auth form.
   *
   * @type {boolean}
   */
  submitted: boolean = false;

  constructor(private readonly security: SecurityService) {
  }

  ngOnInit() {
  }

  /**
   * Try to auth using the current credentials.
   */
  tryAuth() {
    let result = this.security.auth(this.credentials, result => {
      this.submitted = true;
      console.warn("The auth result = " + result)
    });
  }
}
