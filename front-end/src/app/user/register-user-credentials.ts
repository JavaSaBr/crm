import {UserCredentials} from "./user-credentials";
/**
 * The user credentials to register a new user.
 *
 * @author JavaSaBr
 */
export class RegisterUserCredentials extends UserCredentials {

  /**
   * The user confirm password.
   */
  confirmPassword: string;

  constructor(username: string, password: string, repeatPassword: string) {
    super(username, password);
    this.confirmPassword = repeatPassword;
  }
}
