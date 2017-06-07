import {BaseComponent} from "./base.component";
import {AppComponent} from "../app.component";

/**
 * The page component.
 *
 * @author JavaSaBr
 */
export abstract class PageComponent extends BaseComponent {

  constructor() {
    super();
  }

  get minUsernameLength() {
    return AppComponent.MIN_USERNAME_LENGTH;
  }

  get maxUsernameLength() {
    return AppComponent.MAX_USERNAME_LENGTH;
  }

  get minPasswordLength() {
    return AppComponent.MIN_PASSWORD_LENGTH;
  }

  get maxPasswordLength() {
    return AppComponent.MAX_PASSWORD_LENGTH;
  }
}
