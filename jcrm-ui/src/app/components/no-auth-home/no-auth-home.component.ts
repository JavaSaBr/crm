import {Component} from '@angular/core';
import {NavigationEnd, Router} from '@angular/router';

@Component({
    selector: 'app-no-auth-home',
    templateUrl: './no-auth-home.component.html',
    styleUrls: ['./no-auth-home.component.scss'],
    host: {'class': 'flex-column'}
})
export class NoAuthHomeComponent {

    activatedSubPages = false;

    constructor(private readonly router: Router) {
        this.router.events.subscribe(value => {
            if (value instanceof NavigationEnd) {
                //FIXME how it can be done better?
                this.activatedSubPages = value.url.startsWith('/no-auth/');
            }
        });
    }
}
