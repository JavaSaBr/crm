import {Component} from '@angular/core';
import {UserService} from './services/user.service';
import {TranslateService} from '@ngx-translate/core';
import {SecurityService} from './services/security.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent {

    authenticated: boolean;

    constructor(
        private readonly userService: UserService,
        private readonly securityService: SecurityService,
        private readonly translateService: TranslateService
    ) {
        translateService.addLangs(['en', 'ru']);
        translateService.setDefaultLang('en');

        const browserLang = translateService.getBrowserLang();
        translateService.use(browserLang.match(/en|ru/) ? browserLang : 'en');

        securityService.authenticated
            .subscribe(value => this.authenticated = value);
    }
}
