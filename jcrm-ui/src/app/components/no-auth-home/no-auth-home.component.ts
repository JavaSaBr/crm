import {Component} from '@angular/core';
import {NavigationEnd, Router} from '@angular/router';
import {SecurityService} from '../../services/security.service';

@Component({
    selector: 'app-no-auth-home',
    templateUrl: './no-auth-home.component.html',
    styleUrls: ['./no-auth-home.component.scss'],
    host: {'class': 'flex-column'}
})
export class NoAuthHomeComponent {

    activatedSubPages = false;

    constructor(
        private readonly router: Router,
        private readonly securityService: SecurityService
    ) {
        this.router.events.subscribe(value => {
            if (value instanceof NavigationEnd) {
                //FIXME how it can be done better?
                this.activatedSubPages = value.url.startsWith('/no-auth/');
            }
        });

        if (this.securityService.isAuthenticated()) {
            this.router.navigate(['/']);
            return;
        }

        this.securityService.tryToRestoreToken()
            .then(restored => {
                if (restored) {
                    this.router.navigate(['/']);
                }
            });
    }
}
