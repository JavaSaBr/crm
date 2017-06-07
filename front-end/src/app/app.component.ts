import {Component} from "@angular/core";
import {SecurityService} from "./security.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  static readonly MIN_USERNAME_LENGTH: number = 3;
  static readonly MAX_USERNAME_LENGTH: number = 25;
  static readonly MIN_PASSWORD_LENGTH: number = 6;
  static readonly MAX_PASSWORD_LENGTH: number = 25;

  /**
   * The flag that the user is authed.
   */
  private _authed: boolean;

  constructor(private readonly security: SecurityService) {
    this._authed = false;
    this.security.authProperty
      .subscribe(result => this._authed = result)
  }

  logout() {
    this.security.logout();
  }

  /**
   * The flag that the user is authed.
   *
   * @returns {boolean}
   */
  get authed(): boolean {
    return this._authed;
  }
}
