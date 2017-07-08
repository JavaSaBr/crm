import {Injectable} from '@angular/core';
import {Http, RequestOptions} from '@angular/http';
import {UserCredentials} from '../model/user/user-credentials';
import {Utils} from '../util/utils';
import {User} from '../model/user/user';
import {UserRole} from '../model/user/user-role';
import {RegisterUserCredentials} from '../model/user/register-user-credentials';
import {Observable} from 'rxjs/Observable';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {CustomerRegistrationInfo} from '../component/page/customer-register/customer-register.component';
import 'rxjs/add/operator/toPromise';

@Injectable()
export class SecurityService {

  /**
   * The url of auth endpoint.
   *
   * @type {string}
   */
  private static readonly AUTH_URL = '/user-management/authenticate';

  /**
   * The url of register endpoint.
   *
   * @type {string}
   */
  private static readonly REGISTER_URL = '/user-management/register';

  /**
   * The url of customer register endpoint.
   *
   * @type {string}
   */
  private static readonly CUSTOMER_REGISTER_URL = '/customer-management/register';

  /**
   * The name of access token header.
   *
   * @type {string}
   */
  private static readonly ACCESS_TOKEN_HEADER = 'X-Access-Token';

  /**
   * The current user.
   */
  private user: User;

  /**
   * The property to listen the auth flag.
   */
  private readonly _authProperty: BehaviorSubject<boolean>;

  constructor(private readonly http: Http) {
    this._authProperty = new BehaviorSubject(false);
  }

  /**
   * The function to auth an user in the system.
   *
   * @param credentials the user info.
   * @param handler to handle result of authentication.
   */
  public auth(credentials: UserCredentials, handler: (message: string, result: boolean) => void): void {
    const username = credentials.username;
    this.http.post(SecurityService.AUTH_URL, credentials)
      .toPromise()
      .then(response => {
        const body = response.json();
        this.user = new User(username, body.token);
        this._authProperty.next(true);
        handler(null, true);
      })
      .catch(error => Utils.handleErrorMessage(error, (ex: string) => handler(ex, false)));
  }

  /**
   * The function to register an user in the system.
   *
   * @param credentials the user info.
   * @param handler to handle result of registration.
   */
  public register(credentials: RegisterUserCredentials, handler: (message: string, result: boolean) => void): void {
    this.http.post(SecurityService.REGISTER_URL, credentials)
      .toPromise()
      .then(response => handler(null, true))
      .catch(error => Utils.handleErrorMessage(error, (ex: string) => handler(ex, false)));
  }

  /**
   * The function to register a customer in the system.
   *
   * @param info the customer info.
   * @param handler to handle result of registration.
   */
  public registerCustomer(info: CustomerRegistrationInfo, handler: (message: string, result: boolean) => void): void {
    this.http.post(SecurityService.CUSTOMER_REGISTER_URL, info)
      .toPromise()
      .then(response => handler(null, true))
      .catch(error => Utils.handleErrorMessage(error, (ex: string) => handler(ex, false)));
  }

  /**
   * Add access token to header of the request options.
   *
   * @param requestOptions the request options.
   */
  public addAccessToken(requestOptions: RequestOptions): void {

    const accessToken = this.accessToken;

    if (accessToken == null) {
      return;
    }

    requestOptions.headers.append(SecurityService.ACCESS_TOKEN_HEADER, accessToken);
  }

  /**
   * The function to logout from the system.
   */
  logout() {
    this.user = null;
    this._authProperty.next(false);
  }

  /**
   * Get the auth property.
   *
   * @returns {Observable<boolean>}
   */
  get authProperty() {
    return this._authProperty;
  }

  /**
   * Get the current access token.
   *
   * @returns {string} the current access token.
   */
  get accessToken(): string {

    if (this.user == null) {
      return null;
    }

    return this.user.accessToken;
  }

  /**
   * Get the roles of the current user.
   *
   * @returns {UserRole[]}
   */
  get userRoles(): UserRole[] {

    if (this.user == null) {
      return null;
    }

    return this.user.roles;
  }
}
