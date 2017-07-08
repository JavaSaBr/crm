import {Component} from '@angular/core';
import {PageComponent} from '../../page.component';
import {SecurityService} from '../../../service/security.service';
import {Router} from '@angular/router';

@Component({
  moduleId: module.id,
  selector: 'app-register',
  templateUrl: './customer-register.component.html',
  styleUrls: ['./customer-register.component.css'],
})
export class CustomerRegisterComponent extends PageComponent {

  /**
   * The current register user info.
   */
  private _info: CustomerRegistrationInfo;

  /**
   * The error message.
   */
  private _error: string;

  constructor(private readonly security: SecurityService,
              private readonly router: Router) {
    super();
    this._info = new CustomerRegistrationInfo('', '', '');
    this._error = '';
  }

  /**
   * Try to register using the current info.
   */
  tryRegister() {
    this.security.registerCustomer(this._info, (message, result) => {
      if (result) {
        this._error = '';
        this._info.name = '';
        this._info.email = '';
        this._info.phoneNumber = '';
        this.router.navigateByUrl('/login');

      } else {
        this._error = message;
      }
    });
  }

  get info(): CustomerRegistrationInfo {
    return this._info;
  }

  set info(value: CustomerRegistrationInfo) {
    this._info = value;
  }

  get error(): string {
    return this._error;
  }

  set error(value: string) {
    this._error = value;
  }
}

/**
 * The information about a new customer.
 *
 * @author JavaSaBr
 */
export class CustomerRegistrationInfo {

  /**
   * The customer's email.
   */
  email: string;

  /**
   * The customer's name.
   */
  name: string;

  /**
   * The customer's phone number.
   */
  phoneNumber: string;

  constructor(email: string, name: string, phoneNumber: string) {
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
  }
}

