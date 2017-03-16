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
   * The security service.
   */
  security: SecurityService;

  /**
   * The current user credentials.
   */
  credentials: UserCredentials;

  submitted: boolean;

  constructor(security: SecurityService) {
    this.security = security;
    this.credentials = new UserCredentials('', '');
    this.submitted = false;
  }

  ngOnInit() {
  }

  /**
   * Try to auth using the current credentials.
   */
  tryAuth() {
    this.security.auth(this.credentials)
      .subscribe(value => console.info("result = " + value));
  }
}
