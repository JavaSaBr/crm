/**
 * The user model.
 *
 * @author JavaSaBr
 */
export class User {

  /**
   * The current access token.
   */
  private _accessToken: string;

  /**
   * The user name.
   */
  private _name: string;

  /**
   * The user roles.
   */
  private _roles: string[];

  /**
   * Create an user with the name and the access token.
   *
   * @param name the name.
   * @param accessToken the access token.
   * @param roles the roles.
   */
  constructor(name: string, accessToken: string, roles: string[]) {
    this._name = name;
    this._accessToken = accessToken;
    this._roles = roles;
  }

  /**
   * Get an access token of this user or null.
   *
   * @returns {string} the access token.
   */
  get accessToken(): string {
    return this._accessToken;
  }

  /**
   * Get a name of this user.
   *
   * @returns {string} the user name.
   */
  get name(): string {
    return this._name;
  }

  /**
   * Get an available roles of this user.
   *
   * @returns {string[]} the array of available roles.
   */
  get roles(): string[] {
    return this._roles;
  }
}
