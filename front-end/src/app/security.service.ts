import {Injectable} from "@angular/core";
import {Http, Headers, RequestOptions} from "@angular/http";
import {UserCredentials} from "./login/user-credentials";
import {Observable} from "rxjs";
import {Utils} from "./util/Utils";

@Injectable()
export class SecurityService {

  private authUrl = 'user';
  private logoutUrl = 'logout';

  constructor(private http: Http) {
  }

  /**
   * The function to auth an user in the system.
   *
   * @param credentials the user credentials.
   * @returns {Observable<boolean>} the observable result.
   */
  auth(credentials: UserCredentials): Observable<boolean> {

    let headers = new Headers({
      'Authorization': "Basic "
      + btoa(credentials.username + ":" + credentials.password)
    });

    let options = new RequestOptions({
      headers: headers,
      withCredentials: true
    });

    return this.http.get(this.authUrl, options)
      .map(res => res.status == 200).catch(Utils.handleError).first();
  }

  /**
   * The function to logout from the system.
   */
  logout() {
    return this.http.get(this.logoutUrl)
      .map(res => res.status == 200).catch(Utils.handleError).first();
  }
}
