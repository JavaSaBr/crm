import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {SecurityService} from '../../services/security.service';

@Component({
    selector: 'app-no-auth-home',
    templateUrl: './no-auth-home.component.html',
    styleUrls: ['./no-auth-home.component.scss'],
    host: {'class': 'flex-column'}
})
export class NoAuthHomeComponent {

    public static readonly COMPONENT_PATH = 'no-auth';

    activatedSubPages = false;

    constructor(
        private readonly router: Router,
        private readonly securityService: SecurityService
    ) {

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

    onActivateSubPage(componentRef) {
        this.activatedSubPages = true;
    }

    onDeactivateSubPage(componentRef) {
        this.activatedSubPages = false;
    }
}
