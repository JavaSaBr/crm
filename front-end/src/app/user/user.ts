import {UserRole} from './user-role';

/**
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
  private _roles: UserRole[];

  /**
   * Create an user with the name and the access token.
   *
   * @param name the name.
   * @param accessToken the access token.
   */
  constructor(name: string, accessToken: string) {
    this._name = name;
    this._accessToken = accessToken;
  }

  /**
   * Get an access token of this user or null.
   *
   * @returns {string}
   */
  get accessToken(): string {
    return this._accessToken;
  }

  /**
   * Get a name of this user.
   *
   * @returns {string}
   */
  get name(): string {
    return this._name;
  }

  /**
   * Get an available roles of this user.
   *
   * @returns {UserRole[]}
   */
  get roles(): UserRole[] {
    return this._roles;
  }
}
