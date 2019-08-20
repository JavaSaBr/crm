import {Component} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {SecurityService} from '@app/service/security.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent {

    authenticated: boolean;

    constructor(
        private readonly securityService: SecurityService,
        private readonly translateService: TranslateService
    ) {
        this.authenticated = false;

        translateService.addLangs(['en', 'ru']);
        translateService.setDefaultLang('en');

        const browserLang = translateService.getBrowserLang();
        translateService.use(browserLang.match(/en|ru/) ? browserLang : 'en');

        securityService.authenticated
            .subscribe(value => this.authenticated = value);
    }
}
