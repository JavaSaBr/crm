import {Component} from '@angular/core';
import {PageComponent} from '../../page.component';
import {SecurityService} from '../../../security.service';
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
  private _email: string;

  /**
   * The customer's name.
   */
  private _name: string;

  /**
   * The customer's phone number.
   */
  private _phoneNumber: string;

  constructor(email: string, name: string, phoneNumber: string) {
    this._name = name;
    this._email = email;
    this._phoneNumber = phoneNumber;
  }

  get email(): string {
    return this._email;
  }

  set email(value: string) {
    this._email = value;
  }

  get name(): string {
    return this._name;
  }

  set name(value: string) {
    this._name = value;
  }

  get phoneNumber(): string {
    return this._phoneNumber;
  }

  set phoneNumber(value: string) {
    this._phoneNumber = value;
  }
}

