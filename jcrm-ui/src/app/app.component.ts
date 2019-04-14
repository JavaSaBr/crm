import {Component} from '@angular/core';
import {UserService} from './services/user.service';
import {TranslateService} from '@ngx-translate/core';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent {

    constructor(
        private userService: UserService,
        private translate: TranslateService
    ) {
        translate.addLangs(['en', 'ru']);
        translate.setDefaultLang('en');

        const browserLang = translate.getBrowserLang();
        translate.use(browserLang.match(/en|ru/) ? browserLang : 'en');
    }

    isAuthenticated() {
        return this.userService.isAuthenticated();
    }
}
