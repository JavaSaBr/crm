import {Injectable} from "@angular/core";
import {Http, RequestOptions} from "@angular/http";
import {UserCredentials} from "./component/dialog/login/user-credentials";
import {Utils} from "./util/Utils";
import {User} from "./user/user";
import {UserRole} from "./user/user-role";

@Injectable()
export class SecurityService {

  /**
   * The url of auth endpoint.
   *
   * @type {string}
   */
  private static readonly AUTH_URL: string = '/user-management/authenticate';

  /**
   * The name of access token header.
   *
   * @type {string}
   */
  private static readonly ACCESS_TOKEN_HEADER = "X-Access-Token";

  /**
   * The current user.
   */
  private _user: User;

  constructor(private readonly http: Http) {
  }

  /**
   * The function to auth an user in the system.
   *
   * @param credentials the user credentials.
   * @param handler to handle result of authentication.
   * @returns {boolean} true if it was successful.
   */
  public auth(credentials: UserCredentials, handler?: (value: boolean) => void): void {
    let username = credentials.username;
    this.http.post(SecurityService.AUTH_URL, credentials)
      .catch(Utils.handleError).first().subscribe(res => {
      if (res.status == 200) {
        let body = res.json();
        this._user = new User(username, body.token);
        handler.apply(true);
      } else {
        handler.apply(false);
      }
    });
  }

  /**
   * Add access token to header of the request options.
   *
   * @param requestOptions the request options.
   */
  public addAccessToken(requestOptions: RequestOptions): void {
    let accessToken = this.accessToken;
    if (accessToken == null) return;
    requestOptions.headers.append(SecurityService.ACCESS_TOKEN_HEADER, accessToken);
  }

  /**
   * The function to logout from the system.
   */
  logout() {
    this._user = null;
  }

  /**
   * Return true if the user was authed.
   *
   * @returns {boolean}
   */
  isAuthed(): boolean {
    return this.accessToken != null;
  }

  /**
   * Get the current access token.
   *
   * @returns {string} the current access token.
   */
  get accessToken(): string {
    if (this._user == null) return null;
    return this._user.accessToken;
  }

  /**
   * Get the roles of the current user.
   *
   * @returns {UserRole[]}
   */
  get userRoles(): UserRole[] {
    if (this._user == null) return null;
    return this._user.roles;
  }
}
