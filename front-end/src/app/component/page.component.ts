import {BaseComponent} from './base.component';
import {AppComponent} from '../app.component';

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

  get minFirstNameLength() {
    return AppComponent.MIN_FIRST_NAME_LENGTH;
  }

  get maxFirstNameLength() {
    return AppComponent.MAX_FIRST_NAME_LENGTH;
  }

  get minPasswordLength() {
    return AppComponent.MIN_PASSWORD_LENGTH;
  }

  get maxPasswordLength() {
    return AppComponent.MAX_PASSWORD_LENGTH;
  }
}
