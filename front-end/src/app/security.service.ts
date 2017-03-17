import {Injectable} from "@angular/core";
import {Http, RequestOptions} from "@angular/http";
import {UserCredentials} from "./login/user-credentials";
import {Utils} from "./util/Utils";

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
   * The current access token.
   */
  private accessToken: string;

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
    this.http.post(SecurityService.AUTH_URL, credentials)
      .catch(Utils.handleError).first().subscribe(res => {
      if (res.status == 200) {
        let body = res.json();
        this.accessToken = body.token;
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
    requestOptions.headers.append(SecurityService.ACCESS_TOKEN_HEADER, this.getAccessToken());
  }

  /**
   * The function to logout from the system.
   */
  public logout() {
    this.accessToken = null;
  }

  /**
   * Get the current access token.
   *
   * @returns {string} the current access token.
   */
  public getAccessToken(): string {
    return this.accessToken;
  }
}
