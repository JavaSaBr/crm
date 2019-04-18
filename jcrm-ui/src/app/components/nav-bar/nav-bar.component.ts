import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {SideMenuService} from '../../services/side-menu.service';
import {SecurityService} from '../../services/security.service';

@Component({
    selector: 'app-nav-bar',
    templateUrl: './nav-bar.component.html',
    styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent implements OnInit {

    additionalHamburgerStyle: string;
    authenticated: boolean;

    constructor(
        private readonly userService: UserService,
        private readonly securityService: SecurityService,
        private readonly sideMenuService: SideMenuService
    ) {
        this.additionalHamburgerStyle = '';
        this.securityService.authenticated
            .subscribe(value => this.authenticated = value);
    }

    ngOnInit() {

        this.sideMenuService.startOpeningProperty()
            .subscribe(() => {
                this.additionalHamburgerStyle = 'is-active';
            });
        this.sideMenuService.startClosingProperty()
            .subscribe(() => {
                this.additionalHamburgerStyle = '';
            });
    }

    toggleSideMenu() {
        this.sideMenuService.toggleMenu();
    }
}
