/**
 * The user credentials.
 *
 * @author JavaSaBr
 */
export class UserCredentials {

  /**
   * The user name.
   */
  private readonly _username: string;

  /**
   * The user password.
   */
  private readonly _password: string;

  constructor(username: string, password: string) {
    this._password = password;
    this._username = username;
  }

  /**
   * The username.
   *
   * @returns {string}
   */
  get username(): string {
    return this._username;
  }

  /**
   * The password.
   *
   * @returns {string}
   */
  get password(): string {
    return this._password;
  }
}
